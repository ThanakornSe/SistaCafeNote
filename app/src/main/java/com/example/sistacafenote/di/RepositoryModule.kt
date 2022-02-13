package com.example.sistacafenote.di

import com.example.sistacafenote.database.Repository
import org.koin.dsl.module


    val repositoryModule = module {
        single { Repository(get()) }
    }