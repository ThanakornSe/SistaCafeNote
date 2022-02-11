package com.example.sistacafenote.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    val title:String,
    val content:String,
    val imageUri:String = ""
) {
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0L
}