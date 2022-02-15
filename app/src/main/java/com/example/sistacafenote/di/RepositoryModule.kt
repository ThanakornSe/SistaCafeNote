package com.example.sistacafenote.di

import com.example.sistacafenote.data.repository.NoteRepositoryImpl
import com.example.sistacafenote.domain.repository.NoteRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<NoteRepository> { NoteRepositoryImpl(get()) }
}