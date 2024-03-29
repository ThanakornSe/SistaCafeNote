package com.example.sistacafenote.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.sistacafenote.NoteApplication
import com.example.sistacafenote.R
import com.example.sistacafenote.databinding.FragmentEditNoteBinding
import com.example.sistacafenote.model.Note
import com.example.sistacafenote.util.Tag
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class EditNoteFragment : Fragment() {

    private val binding: FragmentEditNoteBinding by lazy {
        FragmentEditNoteBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel: NoteViewModel by viewModel()

    private var imageUri: String? = null
    private lateinit var note: Note

    private var tagEdit: Tag? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            imageUri = it.toString()
            if (imageUri != null && imageUri?.isNotEmpty() == true) {
                binding.flImage.visibility = View.VISIBLE
                Glide.with(requireContext()).load(imageUri).into(binding.imvPhoto)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.apply {
            edtTitle.setText(note.title)
            edtContent.setText(note.content)
            when (note.tag) {
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

        note = EditNoteFragmentArgs.fromBundle(requireArguments()).note

        binding.chipFilter.setOnCheckedChangeListener { _, _ ->
            when {
                binding.chipWork.isChecked -> {
                    viewModel.setTagWork(true)
                }
                binding.chipImportant.isChecked -> {
                    viewModel.setTagImportant(true)
                }
                binding.chipOther.isChecked -> {
                    viewModel.setTagOther(true)
                }
                else -> {
                    viewModel.setTagWork(false)
                    viewModel.setTagImportant(false)
                    viewModel.setTagOther(false)
                }
            }
        }

        viewModel.tagWork.observe(viewLifecycleOwner) {
            if (it) {
                binding.chipWork.isChecked = true
                tagEdit = Tag.WORK
            } else {
                binding.chipWork.isChecked = false
                tagEdit = null
            }
        }

        viewModel.tagImportant.observe(viewLifecycleOwner) {
            if (it) {
                binding.chipImportant.isChecked = true
                tagEdit = Tag.IMPORTANT
            } else {
                binding.chipImportant.isChecked = false
                tagEdit = null
            }
        }

        viewModel.tagOther.observe(viewLifecycleOwner) {
            if (it) {
                binding.chipOther.isChecked = true
                tagEdit = Tag.OTHER
            } else {
                binding.chipOther.isChecked = false
                tagEdit = null
            }
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
                    AlertDialog.Builder(context)
                        .setTitle("Make sure to edit this note?")
                        .setCancelable(false)
                        .setPositiveButton("Yes") { dialogInterface, _ ->
                            viewModel.updateNote(note)
                            dialogInterface.dismiss()
                            this.findNavController()
                                .navigate(EditNoteFragmentDirections.actionEditNoteFragmentToHomeFragment())
                            setFragmentResult("key", bundleOf("tag" to note.tag))
                        }
                        .setNegativeButton("No") { dialogInterface, _ ->
                            dialogInterface.dismiss()
                            this.findNavController()
                                .navigate(EditNoteFragmentDirections.actionEditNoteFragmentToHomeFragment())
                        }
                        .show()
                }
            }

            R.id.uploadImage -> {
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }


}