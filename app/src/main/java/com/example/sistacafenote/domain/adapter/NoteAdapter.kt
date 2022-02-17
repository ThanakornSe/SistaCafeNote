package com.example.sistacafenote.domain.adapter


import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sistacafenote.R
import com.example.sistacafenote.databinding.NoteListItemBinding
import com.example.sistacafenote.domain.model.Note
import com.example.sistacafenote.util.Tag

class NoteAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Note, NoteAdapter.ViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NoteListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = getItem(position)
        holder.binding(note, onClickListener)
    }

    class ViewHolder(private val binding: NoteListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun binding(
            note: Note,
            onClickListener: OnClickListener
        ) {
            binding.noteLayout.setOnClickListener {
                onClickListener.onNoteClick(note)
            }
            binding.txtTitle.text = note.title
            binding.txtContent.text = note.content

            if (note.imageUri.isNotEmpty()) {
                binding.imvThumbnail.visibility = View.VISIBLE
                val uri: Uri = Uri.parse(note.imageUri)
                Glide.with(itemView.context).load(uri)
                    .into(binding.imvThumbnail)
            }else {
                binding.imvThumbnail.visibility = View.GONE
            }

            when (note.tag) {
                Tag.WORK -> {
                    Glide.with(itemView.context).load(R.drawable.color_work_tag)
                        .into(binding.imvTagColor)
                    binding.txtTag.text = "Work"
                }
                Tag.IMPORTANT -> {
                    Glide.with(itemView.context).load(R.drawable.color_important_tag)
                        .into(binding.imvTagColor)
                    binding.txtTag.text = "Important"
                }
                Tag.OTHER -> {
                    Glide.with(itemView.context).load(R.drawable.color_other_tag)
                        .into(binding.imvTagColor)
                    binding.txtTag.text = "Other"
                }
            }
        }
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.title == newItem.title && oldItem.imageUri == newItem.imageUri &&
                    oldItem.content == newItem.content && oldItem.tag == newItem.tag
        }
    }
}

interface OnClickListener {
    fun onNoteClick(note: Note)
}