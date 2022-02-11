package com.example.sistacafenote.adapter


import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sistacafenote.R
import com.example.sistacafenote.databinding.NoteListItemBinding
import com.example.sistacafenote.model.Note
import com.example.sistacafenote.util.Tag

class NoteAdapter: ListAdapter<Note,NoteAdapter.ViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = NoteListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = getItem(position)
        holder.binding(note)
    }


    class ViewHolder(private val binding:NoteListItemBinding):RecyclerView.ViewHolder(binding.root){
        fun binding(note:Note){

            binding.txtTitle.text = note.title
            binding.txtContent.text = note.content

            val uri: Uri = Uri.parse(note.imageUri)
            Glide.with(itemView.context).load(uri)
                .into(binding.imvThumbnail)

            when (note.tag){
                Tag.WORK -> {
                    Glide.with(itemView.context).load(R.drawable.color_work_tag)
                        .into(binding.imvTagColor)
                }
                Tag.IMPORTANT -> {
                    Glide.with(itemView.context).load(R.drawable.color_important_tag)
                        .into(binding.imvTagColor)
                }
                Tag.OTHER -> {
                    Glide.with(itemView.context).load(R.drawable.color_other_tag)
                        .into(binding.imvTagColor)
                }
            }


        }
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Note>(){
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.content == newItem.content
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

    }
}