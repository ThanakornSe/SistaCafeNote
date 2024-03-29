package com.example.sistacafenote

import android.app.Application
import com.example.sistacafenote.database.NoteDatabase
import com.example.sistacafenote.database.Repository
import com.example.sistacafenote.di.dbModule
import com.example.sistacafenote.di.repositoryModule
import com.example.sistacafenote.di.viewModelModule
import com.example.sistacafenote.ui.NoteViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class NoteApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NoteApplication)
            modules(dbModule, repositoryModule, viewModelModule)
        }
    }

}