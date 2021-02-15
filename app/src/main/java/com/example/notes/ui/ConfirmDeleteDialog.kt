package com.example.notes.ui

import android.app.Application
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.notes.db.NoteDatabase
import com.example.notes.repo.NoteRepo

class ConfirmDeleteDialog: DialogFragment() {


    val dao = NoteDatabase.getInstance(context as Application?).noteDao
    val repo = NoteRepo(dao)
    val noteViewModel: NoteViewModel by viewModels { NoteViewModelFactory(repo, this) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle("Delete all")
            .setMessage("All notes will be deleted")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Yes") {_,_ ->
                noteViewModel.deleteAll()
            }
            .create()

    companion object {
        const val TAG = "ConfirmDeleteDialog"
    }
}