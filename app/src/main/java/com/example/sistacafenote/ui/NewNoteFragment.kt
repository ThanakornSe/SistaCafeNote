package com.example.sistacafenote.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.sistacafenote.R
import com.example.sistacafenote.databinding.FragmentHomeBinding
import com.example.sistacafenote.databinding.FragmentNewNoteBinding


class NewNoteFragment : Fragment() {

    private lateinit var binding: FragmentNewNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_new_note, container, false)

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

        return binding.root
    }

}