package com.example.sistacafenote.di

import com.example.sistacafenote.note.data.data_source.NoteDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {
    single { NoteDatabase.getDatabase(androidContext()).noteDao }
}
