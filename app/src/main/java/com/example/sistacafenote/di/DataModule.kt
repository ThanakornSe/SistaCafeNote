package com.example.sistacafenote.di

import androidx.room.Room
import com.example.sistacafenote.database.NoteDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {
    single { Room.databaseBuilder(androidContext(),NoteDatabase::class.java,"note_database.db").build().noteDao() }
    //single { NoteDatabase.getDatabase(androidContext()).noteDao }
}
