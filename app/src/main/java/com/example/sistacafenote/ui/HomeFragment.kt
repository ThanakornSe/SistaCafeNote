package com.example.sistacafenote.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.sistacafenote.NoteApplication
import com.example.sistacafenote.adapter.NoteAdapter
import com.example.sistacafenote.adapter.OnClickListener
import com.example.sistacafenote.databinding.FragmentHomeBinding
import com.example.sistacafenote.model.Note
import com.example.sistacafenote.util.AppConstant.KEY_VALUE
import com.example.sistacafenote.util.AppConstant.REQUEST_KEY
import com.example.sistacafenote.util.Tag
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.inject

class HomeFragment : Fragment(), OnClickListener {

    private val binding: FragmentHomeBinding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private lateinit var adapter: NoteAdapter
    private val viewModel: NoteViewModel by viewModel()
    private var tag: Tag = Tag.OTHER

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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
                .navigate(HomeFragmentDirections.actionHomeFragmentToNewNoteFragment(tag))
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
            HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(note)
        )
    }
}