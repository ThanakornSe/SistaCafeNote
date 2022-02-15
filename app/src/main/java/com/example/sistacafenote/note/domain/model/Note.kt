package com.example.sistacafenote.note.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sistacafenote.util.Tag
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "note_table")
@Parcelize
data class Note(
    var title: String,
    var content: String,
    var tag: Tag = Tag.OTHER,
    var imageUri: String = ""
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}