package com.example.sistacafenote.util

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun tagToString(tag: Tag): String {
        return tag.name
    }

    @TypeConverter
    fun stringToTag(tag: String): Tag {
        return Tag.valueOf(tag)
    }
}