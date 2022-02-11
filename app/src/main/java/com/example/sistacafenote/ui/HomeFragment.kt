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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sistacafenote.NoteApplication
import com.example.sistacafenote.R
import com.example.sistacafenote.adapter.NoteAdapter
import com.example.sistacafenote.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter:NoteAdapter

    private val viewModel:NoteViewModel by viewModels {
        val application = requireNotNull(this.activity).application
        NoteViewModelFactory((application as NoteApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false)

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

        adapter = NoteAdapter()

        viewModel.allNote.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.rvNoteList.adapter = adapter

        binding.fabNewNote.setOnClickListener {
            this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNewNoteFragment())
        }

        return binding.root
    }



}