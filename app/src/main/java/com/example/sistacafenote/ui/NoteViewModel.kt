package com.example.sistacafenote.ui


import androidx.lifecycle.*
import com.example.sistacafenote.database.Repository
import com.example.sistacafenote.model.Note
import com.example.sistacafenote.util.Tag
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: Repository) : ViewModel() {

    val allNote: LiveData<List<Note>> = repository.allNote.asLiveData()

    fun getNoteByTag(tag: Tag):LiveData<List<Note>> = repository.getNoteByTag(tag).asLiveData()

    fun insertNote(note: Note) = viewModelScope.launch { repository.insertNote(note) }
    fun updateNote(note: Note) = viewModelScope.launch { repository.updateNote(note) }
    fun deleteNote(note: Note) = viewModelScope.launch { repository.deleteNote(note) }



}