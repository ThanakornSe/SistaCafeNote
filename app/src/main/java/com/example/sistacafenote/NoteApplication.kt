package com.example.sistacafenote

import android.app.Application
import com.example.sistacafenote.di.dbModule
import com.example.sistacafenote.di.repositoryModule
import com.example.sistacafenote.di.useCaseModule
import com.example.sistacafenote.di.viewModelModule
import com.example.sistacafenote.data.data_source.NoteDatabase
import com.example.sistacafenote.data.repository.NoteRepositoryImpl
import com.example.sistacafenote.presentation.viewmodel.NoteViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class NoteApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NoteApplication)
            modules(dbModule, repositoryModule, useCaseModule, viewModelModule)
        }
    }

}