package com.example.sistacafenote.di

import com.example.sistacafenote.domain.use_case.NoteUseCase
import org.koin.dsl.module


val useCaseModule = module {
    single { NoteUseCase(get()) }
}