package com.example.sistacafenote.note.presentation.ui

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.sistacafenote.R
import com.example.sistacafenote.databinding.FragmentNewNoteBinding
import com.example.sistacafenote.note.domain.model.Note
import com.example.sistacafenote.note.presentation.viewmodel.NoteViewModel
import com.example.sistacafenote.util.AppConstant.KEY_VALUE
import com.example.sistacafenote.util.AppConstant.REQUEST_KEY
import com.example.sistacafenote.util.Tag
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewNoteFragment : Fragment() {

    private val binding: FragmentNewNoteBinding by lazy {
        FragmentNewNoteBinding.inflate(layoutInflater)
    }

    private val viewModel: NoteViewModel by viewModel()
    private var tagEdit: Tag? = null
    private var imageUri: String? = null

    private val selectedImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                imageUri = result.data?.data.toString()
                if (imageUri != null && imageUri?.isNotEmpty() == true) {
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
        setHasOptionsMenu(true)
        when (NewNoteFragmentArgs.fromBundle(requireArguments()).tag) {
            Tag.WORK -> viewModel.setTagWork(true)
            Tag.IMPORTANT -> viewModel.setTagImportant(true)
            Tag.OTHER -> viewModel.setTagOther(true)
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
                tagEdit = Tag.WORK
            } else tagEdit = null
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
                tagEdit = Tag.IMPORTANT
            } else tagEdit = null
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
                tagEdit = Tag.OTHER
            } else tagEdit = null
        }

        binding.btnDeleteImage.setOnClickListener {
            imageUri = null
            binding.flImage.visibility = View.GONE
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
                val image = imageUri ?: ""
                val tag = tagEdit ?: Tag.OTHER
                if (title.isNotEmpty() && content.isNotEmpty()) {
                    viewModel.insertNote(Note(title, content, tag, imageUri = image))
                    this.findNavController()
                        .navigate(NewNoteFragmentDirections.actionNewNoteFragmentToHomeFragment())
                    setFragmentResult(REQUEST_KEY, bundleOf(KEY_VALUE to tag))
                } else {
                    AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage("Please fill the information.")
                        .setCancelable(false)
                        .setPositiveButton("Dismiss") { dialogInterface, _ -> dialogInterface.dismiss() }
                        .show()
                }
            }
            R.id.uploadImage -> {
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also {
                    selectedImage.launch(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}