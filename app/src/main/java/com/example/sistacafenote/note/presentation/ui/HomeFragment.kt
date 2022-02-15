package com.example.sistacafenote.note.presentation.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.sistacafenote.R
import com.example.sistacafenote.databinding.FragmentHomeBinding
import com.example.sistacafenote.note.domain.adapter.NoteAdapter
import com.example.sistacafenote.note.domain.adapter.OnClickListener
import com.example.sistacafenote.note.domain.model.Note
import com.example.sistacafenote.note.presentation.viewmodel.NoteViewModel
import com.example.sistacafenote.util.AppConstant.KEY_VALUE
import com.example.sistacafenote.util.AppConstant.REQUEST_KEY
import com.example.sistacafenote.util.Tag
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), OnClickListener {

    private val binding: FragmentHomeBinding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private lateinit var adapter: NoteAdapter
    private val viewModel: NoteViewModel by viewModel()
    private var tag: Tag = Tag.OTHER
    private lateinit var deleteIcon: Drawable
    private var swipeBackground: ColorDrawable =
        ColorDrawable(Color.parseColor("#C11642"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        deleteIcon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_delete)!!

        setFragmentResultListener(REQUEST_KEY) { _, bundle ->
            when (bundle.get(KEY_VALUE)) {
                Tag.OTHER -> viewModel.setTagOther(true)
                Tag.IMPORTANT -> viewModel.setTagImportant(true)
                Tag.WORK -> viewModel.setTagWork(true)
            }
        }

        binding.chipWork.setOnClickListener {
            it.isSelected = it.isSelected == false
            if (it.isSelected) {
                viewModel.setTagWork(true)
            } else viewModel.setTagWork(false)
        }
        viewModel.tagWork.observe(viewLifecycleOwner) {
            if (it) {
                binding.chipOther.isSelected = false
                binding.chipWork.isSelected = true
                binding.chipImportant.isSelected = false
                getListByTag(Tag.WORK)
                tag = Tag.WORK
            } else getListByTag(null)
        }

        binding.chipImportant.setOnClickListener {
            it.isSelected = it.isSelected == false
            if (it.isSelected) {
                viewModel.setTagImportant(true)
            } else viewModel.setTagImportant(false)
        }
        viewModel.tagImportant.observe(viewLifecycleOwner) {
            if (it) {
                binding.chipOther.isSelected = false
                binding.chipWork.isSelected = false
                binding.chipImportant.isSelected = true
                getListByTag(Tag.IMPORTANT)
                tag = Tag.IMPORTANT
            } else getListByTag(null)
        }

        binding.chipOther.setOnClickListener {
            it.isSelected = it.isSelected == false
            if (it.isSelected) {
                viewModel.setTagOther(true)
            } else viewModel.setTagOther(false)
        }
        viewModel.tagOther.observe(viewLifecycleOwner) {
            if (it) {
                binding.chipOther.isSelected = true
                binding.chipWork.isSelected = false
                binding.chipImportant.isSelected = false
                getListByTag(Tag.OTHER)
                tag = Tag.OTHER
            } else getListByTag(null)
        }

        adapter = NoteAdapter(this)
        binding.rvNoteList.adapter = adapter

        binding.fabNewNote.setOnClickListener {
            this.findNavController()
                .navigate(
                    HomeFragmentDirections.actionHomeFragmentToNewNoteFragment(
                        tag
                    )
                )
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val note = adapter.currentList[position]
                viewModel.deleteNote(note)
                //Undo
                Snackbar.make(
                    binding.rvNoteList,
                    "SuccessFully deleted articled",
                    Snackbar.LENGTH_SHORT
                ).apply {
                    setAction("Undo") {
                        viewModel.insertNote(note)
                    }
                    show()
                }
            }


            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

                if (dX > 0) {
                    swipeBackground.setBounds(
                        itemView.left,
                        itemView.top,
                        dX.toInt(),
                        itemView.bottom
                    )
                    deleteIcon.setBounds(
                        itemView.left + iconMargin,
                        itemView.top + iconMargin,
                        itemView.left + iconMargin + deleteIcon.intrinsicWidth,
                        itemView.bottom - iconMargin
                    )
                } else {
                    swipeBackground.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    deleteIcon.setBounds(
                        itemView.right - iconMargin - deleteIcon.intrinsicWidth,
                        itemView.top + iconMargin,
                        itemView.right - iconMargin,
                        itemView.bottom - iconMargin
                    )
                }
                swipeBackground.draw(c)
                c.save()

                if (dX > 0) {
                    c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                } else {
                    c.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                }

                deleteIcon.draw(c)
                c.restore()
                super.onChildDraw(
                    c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
                )
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvNoteList)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        when {
            binding.chipWork.isSelected -> {
                viewModel.setTagWork(true)
            }
            binding.chipImportant.isSelected -> {
                viewModel.setTagImportant(true)
            }
            binding.chipOther.isSelected -> {
                viewModel.setTagOther(true)
            }
            else -> {
                viewModel.setTagWork(false)
                viewModel.setTagImportant(false)
                viewModel.setTagOther(false)
            }
        }
    }

    private fun getListByTag(tag: Tag?) {
        viewModel.getNoteByTag(tag).observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onNoteClick(note: Note) {
        this.findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(
                note
            )
        )
    }
}