package com.example.notes.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM  notes_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM notes_table")
    fun getAllNotes(): LiveData<List<Note>>

}