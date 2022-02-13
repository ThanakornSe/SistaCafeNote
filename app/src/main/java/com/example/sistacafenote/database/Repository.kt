package com.example.sistacafenote.database

import androidx.lifecycle.LiveData
import com.example.sistacafenote.model.Note
import com.example.sistacafenote.util.Tag
import kotlinx.coroutines.flow.Flow

class Repository(private val noteDao: NoteDao) {

    val allNote = noteDao.getAllNote()

    fun getNoteByTag(tag: Tag):Flow<List<Note>> = noteDao.getNoteByTAG(tag)

    suspend fun insertNote(note:Note){
        noteDao.insertNote(note)
    }

    suspend fun updateNote(note:Note){
        noteDao.updateNote(note)
    }

    suspend fun deleteNote(note:Note){
        noteDao.deleteNote(note)
    }

}