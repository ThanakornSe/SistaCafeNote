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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.sistacafenote.NoteApplication
import com.example.sistacafenote.R
import com.example.sistacafenote.databinding.FragmentEditNoteBinding
import com.example.sistacafenote.model.Note


class EditNoteFragment : Fragment() {

    private lateinit var binding: FragmentEditNoteBinding

    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(NoteApplication.instance.repository)
    }

    private lateinit var imageUri: String
    private lateinit var note:Note

    private val selectedImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val title = binding.edtTitle.toString()
                val content = binding.edtContent.toString()
                imageUri = result.data?.data.toString()
                if (title.isNotEmpty() && content.isNotEmpty()) {
                    viewModel.insertNote(Note(title, content, imageUri = imageUri))
                }
                Glide.with(requireContext()).load(result.data?.data)
                    .into(binding.imvPhoto)
            }
        }



    override fun onResume() {
        super.onResume()

        if (note != null){
                binding.apply {
                    edtTitle.setText(note.title)
                    edtContent.setText(note.content)
//                    note.imageUri?.let {
//                        binding.flImage.visibility = View.VISIBLE
//                        val uri: Uri = Uri.parse(note.imageUri)
//                        Glide.with(requireContext()).load(uri)
//                            .into(binding.imvPhoto)
//                    }
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
                if (title.isNotEmpty() && content.isNotEmpty()) {
                    note.title = title
                    note.content = content
                    viewModel.updateNote(note)
                }
                this.findNavController()
                    .navigate(EditNoteFragmentDirections.actionEditNoteFragmentToHomeFragment())
            }

            R.id.uploadImage -> {
//                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also {
//                    selectedImage.launch(it)
//                }
            }
        }

        return super.onOptionsItemSelected(item)
    }


}