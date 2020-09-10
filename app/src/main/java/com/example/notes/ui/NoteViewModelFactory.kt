package com.example.notes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notes.repo.NoteRepo

class NoteViewModelFactory(private val repo: NoteRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(repo) as T
        }

        throw IllegalArgumentException("Unknown view model class")
    }
}