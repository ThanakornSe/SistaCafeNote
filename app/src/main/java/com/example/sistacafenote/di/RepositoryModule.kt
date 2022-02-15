package com.example.sistacafenote.di

import com.example.sistacafenote.note.data.repository.NoteRepositoryImpl
import com.example.sistacafenote.note.domain.repository.NoteRepository
import org.koin.dsl.module

val repositoryModule = module {

    ///single { NoteRepositoryImpl(get()) }
    single<NoteRepository> { NoteRepositoryImpl(get()) }
}