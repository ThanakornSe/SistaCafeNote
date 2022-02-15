package com.example.sistacafenote.domain.repository

import com.example.sistacafenote.domain.model.Note
import com.example.sistacafenote.util.Tag
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun allNote():Flow<List<Note>>

    fun getNoteByTag(tag: Tag): Flow<List<Note>>

    suspend fun insertNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(note: Note)

}