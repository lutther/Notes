package com.example.notes.db

import androidx.room.*
import com.example.notes.ui.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    fun getAllNotes(sortOrder: SortOrder): Flow<List<Note>> =
        when(sortOrder) {
            SortOrder.BY_NAME -> getAllNotesByName()
            SortOrder.BY_DATE -> getAllNotesByDate()
        }


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM  notes_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM notes_table ORDER BY note ASC")
    fun getAllNotesByName(): Flow<List<Note>>

    @Query("SELECT * FROM notes_table")
    fun getAllNotesByDate(): Flow<List<Note>>

    @Query("SELECT * FROM notes_table WHERE note LIKE :searchQuery")
    fun searchNotes(searchQuery: String): Flow<List<Note>>

}