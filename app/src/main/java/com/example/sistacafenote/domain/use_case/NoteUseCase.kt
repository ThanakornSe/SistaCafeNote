package com.example.sistacafenote.domain.use_case

import com.example.sistacafenote.domain.model.Note
import com.example.sistacafenote.domain.repository.NoteRepository
import com.example.sistacafenote.util.Tag
import kotlinx.coroutines.flow.Flow

class NoteUseCase(private val repository: NoteRepository) {

    fun allNote(): Flow<List<Note>> = repository.allNote()

    fun getNoteByTag(tag: Tag): Flow<List<Note>> = repository.getNoteByTag(tag)

    suspend fun insertNote(note: Note)  = repository.insertNote(note)

    suspend fun updateNote(note: Note) = repository.updateNote(note)

    suspend fun deleteNote(note: Note) = repository.deleteNote(note)

}