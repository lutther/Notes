package com.example.notes.repo

import com.example.notes.db.Note
import com.example.notes.db.NoteDao
import com.example.notes.ui.SortOrder
import kotlinx.coroutines.flow.Flow

class NoteRepo(private val dao: NoteDao) {


    fun notes(sortOrder: SortOrder) = dao.getAllNotes(sortOrder)

    suspend fun insert(note: Note) {
        dao.addNote(note)
    }

    suspend fun update(note: Note) {
        dao.updateNote(note)
    }

    suspend fun delete(note: Note) {
        dao.deleteNote(note)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    fun searchNotes(searchQuery: String): Flow<List<Note>> {
        return dao.searchNotes(searchQuery)
    }
}