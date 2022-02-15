package com.example.sistacafenote.note.data.data_source

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.sistacafenote.note.domain.model.Note
import com.example.sistacafenote.util.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM note_table")
    suspend fun clearNote()

    @Query("SELECT * FROM note_table ORDER BY id DESC")
    fun getAllNote(): Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE tag=:tag ORDER BY id DESC ")
    fun getNoteByTAG(tag: Tag): Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE id=:id")
    fun getNoteById(id:Long):LiveData<Note>
}