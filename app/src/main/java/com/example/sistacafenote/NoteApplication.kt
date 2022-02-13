package com.example.sistacafenote

import android.app.Application
import com.example.sistacafenote.di.dbModule
import com.example.sistacafenote.di.repositoryModule
import com.example.sistacafenote.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NoteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NoteApplication)
            modules(listOf(dbModule, repositoryModule, viewModelModule))
        }
    }
}