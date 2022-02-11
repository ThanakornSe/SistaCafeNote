package com.example.sistacafenote.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.sistacafenote.database.Repository
import com.example.sistacafenote.model.Note
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: Repository):ViewModel() {

    val allNote:LiveData<List<Note>> = repository.allNoteItem.asLiveData()

    init {
        Log.d("itSize", "onCreateView: ${allNote.value?.size}")
    }

    fun insertNote(note:Note) = viewModelScope.launch { repository.insertNote(note) }
    fun updateNote(note:Note) = viewModelScope.launch { repository.updateNote(note) }
    fun deleteNote(note:Note) = viewModelScope.launch { repository.deleteNote(note) }




}