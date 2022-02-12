package com.example.sistacafenote.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.sistacafenote.NoteApplication
import com.example.sistacafenote.R
import com.example.sistacafenote.databinding.FragmentEditNoteBinding
import com.example.sistacafenote.model.Note
import com.example.sistacafenote.util.Tag


class EditNoteFragment : Fragment() {

    private lateinit var binding: FragmentEditNoteBinding

    private val viewModel: NoteViewModel by viewModels {
        NoteViewModelFactory(NoteApplication.instance.repository)
    }

    private var imageUri: String? = null
    private lateinit var note: Note

    private var tagEdit:Tag? = null

    private val selectedImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                imageUri = result.data?.data.toString()
            }
        }


    override fun onResume() {
        super.onResume()
        binding.apply {
            edtTitle.setText(note.title)
            edtContent.setText(note.content)
            when (note.tag){
                Tag.OTHER -> viewModel.setTagOther(true)
                Tag.IMPORTANT -> viewModel.setTagImportant(true)
                Tag.WORK -> viewModel.setTagWork(true)
            }
            if (note.imageUri.isNotEmpty() && imageUri == null) {
                binding.flImage.visibility = View.VISIBLE
                val uri: Uri = Uri.parse(note.imageUri)
                Glide.with(requireContext()).load(uri)
                    .into(binding.imvPhoto)
            } else if (imageUri != null) {
                imageUri?.let {
                    binding.flImage.visibility = View.VISIBLE
                    viewModel.setImageUri(it)
                    viewModel.imageUri.observe(viewLifecycleOwner) { uri ->
                        Glide.with(requireContext()).load(Uri.parse(uri))
                            .into(binding.imvPhoto)
                    }
                }
            } else {
                binding.flImage.visibility = View.GONE
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_note, container, false)

        note = EditNoteFragmentArgs.fromBundle(requireArguments()).note

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
                tagEdit = Tag.WORK
            }else tagEdit = null
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
                tagEdit = Tag.IMPORTANT
            }else tagEdit = null
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
                tagEdit = Tag.OTHER
            }else tagEdit = null
        }

        binding.btnDeleteImage.setOnClickListener {
            imageUri = null
            note.imageUri = ""
            viewModel.setImageUri("")
            viewModel.updateNote(note)
            binding.flImage.visibility = View.GONE
        }

        setHasOptionsMenu(true)
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
                val image = imageUri ?: note.imageUri
                if (title.isNotEmpty() && content.isNotEmpty()) {
                    note.title = title
                    note.content = content
                    note.imageUri = image
                    note.tag = tagEdit ?: Tag.OTHER
                    viewModel.updateNote(note)
                }
                this.findNavController()
                    .navigate(EditNoteFragmentDirections.actionEditNoteFragmentToHomeFragment())
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