package com.example.sistacafenote.ui

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.sistacafenote.NoteApplication
import com.example.sistacafenote.R
import com.example.sistacafenote.databinding.FragmentHomeBinding
import com.example.sistacafenote.databinding.FragmentNewNoteBinding
import com.example.sistacafenote.model.Note
import kotlinx.android.synthetic.main.fragment_new_note.*


class NewNoteFragment : Fragment() {

    private lateinit var binding: FragmentNewNoteBinding

    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(NoteApplication.instance.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private lateinit var imageUri: String

    private val selectedImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                imageUri = result.data?.data.toString()
                imageUri?.let {
                    binding.flImage.visibility = View.VISIBLE
                    Glide.with(requireContext()).load(result.data?.data)
                        .into(binding.imvPhoto)
                }
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_note, container, false)

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

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (menu as MenuBuilder).setOptionalIconsVisible(true)
        inflater.inflate(R.menu.new_note_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.saveNote -> {
                val title = binding.edtTitle.text.toString()
                val content = binding.edtContent.text.toString()
                if (title.isNotEmpty() && content.isNotEmpty()) {
                    viewModel.insertNote(Note(title, content))
                }
                this.findNavController()
                    .navigate(NewNoteFragmentDirections.actionNewNoteFragmentToHomeFragment())
            }

            R.id.uploadImage -> {
//                Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also {
//                    selectedImage.launch(it)
//                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

}