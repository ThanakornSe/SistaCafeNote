package com.example.sistacafenote.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistacafenote.NoteApplication
import com.example.sistacafenote.R
import com.example.sistacafenote.adapter.NoteAdapter
import com.example.sistacafenote.adapter.OnClickListener
import com.example.sistacafenote.databinding.FragmentHomeBinding
import com.example.sistacafenote.model.Note
import com.example.sistacafenote.util.Tag
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment(),OnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: NoteAdapter

    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(NoteApplication.instance.repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.chipWork.setOnClickListener {
            it.isSelected = it.isSelected == false
            binding.chipImportant.isSelected = false
            binding.chipOther.isSelected = false
        }

        binding.chipImportant.setOnClickListener {
            it.isSelected = it.isSelected == false
            binding.chipWork.isSelected = false
            binding.chipOther.isSelected = false
        }

        binding.chipOther.setOnClickListener {
            it.isSelected = it.isSelected == false
            binding.chipWork.isSelected = false
            binding.chipImportant.isSelected = false
        }

        adapter = NoteAdapter(this)

        viewModel.allNote.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        binding.rvNoteList.adapter = adapter

        binding.fabNewNote.setOnClickListener {
            this.findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToNewNoteFragment())
            viewModel.setEdit(false)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val note = adapter.currentList[position]
                viewModel.deleteNote(note)

                //Undo
                Snackbar.make(binding.rvNoteList,"SuccessFully deleted articled",Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
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

    override fun onNoteClick(note: Note) {
        this.findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(note)
        )

    }


}