package com.example.sistacafenote.note.presentation.viewmodel


import androidx.lifecycle.*
import com.example.sistacafenote.note.data.repository.NoteRepositoryImpl
import com.example.sistacafenote.note.domain.model.Note
import com.example.sistacafenote.note.domain.repository.NoteRepository
import com.example.sistacafenote.note.domain.use_case.NoteUseCase
import com.example.sistacafenote.util.Tag
import kotlinx.coroutines.launch

class NoteViewModel(private val noteUseCase: NoteUseCase) : ViewModel() {

    fun getNoteByTag(tag: Tag? = null): LiveData<List<Note>> = tag?.let {
        noteUseCase.getNoteByTag(tag).asLiveData()
    } ?: run { noteUseCase.allNote().asLiveData() }

    private val _imageUri = MutableLiveData<String>()
    val imageUri: LiveData<String> get() = _imageUri
    fun setImageUri(uri:String){
        _imageUri.value = uri
    }

    private val _tagOther = MutableLiveData<Boolean>()
    val tagOther: LiveData<Boolean> get() = _tagOther
    fun setTagOther(b:Boolean){
        _tagOther.value = b
    }

    private val _tagImportant = MutableLiveData<Boolean>()
    val tagImportant: LiveData<Boolean> get() = _tagImportant
    fun setTagImportant(b:Boolean){
        _tagImportant.value = b
    }

    private val _tagWork = MutableLiveData<Boolean>()
    val tagWork: LiveData<Boolean> get() = _tagWork
    fun setTagWork(b:Boolean){
        _tagWork.value = b
    }

    fun insertNote(note: Note) = viewModelScope.launch { noteUseCase.insertNote(note) }
    fun updateNote(note: Note) = viewModelScope.launch { noteUseCase.updateNote(note) }
    fun deleteNote(note: Note) = viewModelScope.launch { noteUseCase.deleteNote(note) }

}