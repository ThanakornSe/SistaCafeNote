package com.example.sistacafenote.note.data.repository

import com.example.sistacafenote.note.domain.model.Note
import com.example.sistacafenote.note.data.data_source.NoteDao
import com.example.sistacafenote.note.domain.repository.NoteRepository
import com.example.sistacafenote.util.Tag
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(private val noteDao: NoteDao):NoteRepository {

    override fun allNote(): Flow<List<Note>> = noteDao.getAllNote()

    override fun getNoteByTag(tag: Tag): Flow<List<Note>> = noteDao.getNoteByTAG(tag)

    override suspend fun insertNote(note: Note)  = noteDao.insertNote(note)

    override suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    override suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)
}