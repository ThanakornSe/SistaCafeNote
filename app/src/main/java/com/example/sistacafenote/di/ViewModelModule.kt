package com.example.sistacafenote.di

import com.example.sistacafenote.note.presentation.viewmodel.NoteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { NoteViewModel(get()) }
}