package com.example.sistacafenote.di

import com.example.sistacafenote.database.NoteDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {
    single { NoteDatabase.getDatabase(androidContext()).noteDao }
}
