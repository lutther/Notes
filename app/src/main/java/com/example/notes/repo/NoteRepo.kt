package com.example.notes.repo

import com.example.notes.db.Note
import com.example.notes.db.NoteDao

class NoteRepo(private val dao: NoteDao) {
    val notes = dao.getAllNotes()

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
}