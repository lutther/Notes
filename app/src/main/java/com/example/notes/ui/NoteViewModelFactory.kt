package com.example.notes.ui//package com.example.notes.ui

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.example.notes.repo.NoteRepo

class NoteViewModelFactory(
    val repo: NoteRepo,
    private val owner: SavedStateRegistryOwner,
    private val defaultArgs: Bundle? = null
): AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T = NoteViewModel(repo, handle) as T

}