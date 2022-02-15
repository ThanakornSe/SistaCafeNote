package com.example.sistacafenote.di

import com.example.sistacafenote.note.domain.use_case.*
import org.koin.dsl.module


val useCaseModule = module {
    single { NoteUseCase(get()) }
}