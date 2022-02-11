package com.example.sistacafenote

import android.app.Application
import com.example.sistacafenote.database.NoteDatabase
import com.example.sistacafenote.database.Repository

class NoteApplication:Application() {
    private val database by lazy { NoteDatabase.getDatabase(this) }
    val repository by lazy { Repository(database.noteDao) }
}