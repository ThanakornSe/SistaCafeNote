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
import androidx.lifecycle.ViewModelProvider
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

    private val viewModel: NoteViewModel by viewModels {
        NoteViewModelFactory(NoteApplication.instance.repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.chipWork.setOnClickListener {
            it.isSelected = it.isSelected == false
            if (it.isSelected){
                viewModel.setTagWork(true)
            }else viewModel.setTagWork(false)
        }
        viewModel.tagWork.observe(viewLifecycleOwner){
            if (it){
                binding.chipOther.isSelected = false
                binding.chipWork.isSelected = true
                binding.chipImportant.isSelected = false
                getListByTag(Tag.WORK)
            }else getListByTag(null)
        }

        binding.chipImportant.setOnClickListener {
            it.isSelected = it.isSelected == false
            if (it.isSelected){
                viewModel.setTagImportant(true)
            }else viewModel.setTagImportant(false)
        }
        viewModel.tagImportant.observe(viewLifecycleOwner){
            if (it){
                binding.chipOther.isSelected = false
                binding.chipWork.isSelected = false
                binding.chipImportant.isSelected = true
                getListByTag(Tag.IMPORTANT)
            }else getListByTag(null)
        }

        binding.chipOther.setOnClickListener {
            it.isSelected = it.isSelected == false
            if (it.isSelected){
                viewModel.setTagOther(true)
            }else viewModel.setTagOther(false)

        }
        viewModel.tagOther.observe(viewLifecycleOwner){
            if (it){
                binding.chipOther.isSelected = true
                binding.chipWork.isSelected = false
                binding.chipImportant.isSelected = false
                getListByTag(Tag.OTHER)
            }else getListByTag(null)
        }

        adapter = NoteAdapter(this)

        getListByTag(null)

        binding.rvNoteList.adapter = adapter

        binding.fabNewNote.setOnClickListener {
            this.findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToNewNoteFragment())
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