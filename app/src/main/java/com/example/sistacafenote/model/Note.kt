package com.example.sistacafenote.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sistacafenote.util.Tag
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "note_table")
@Parcelize
data class Note(
    val title:String,
    val content:String,
    val tag: Tag = Tag.OTHER,
    val imageUri:String = ""
):Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0L
}