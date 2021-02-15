package com.example.notes.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notes.R
import com.example.notes.databinding.FragmentWriteNoteBinding
import com.example.notes.db.NoteDatabase
import com.example.notes.repo.NoteRepo
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect

class WriteNote : Fragment(R.layout.fragment_write_note) {

    lateinit var noteViewModel: NoteViewModel
    val args: WriteNoteArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentWriteNoteBinding.bind(view)

        val dao = NoteDatabase.getInstance(activity?.application).noteDao
        val repo = NoteRepo(dao)
        val factory = NoteViewModelFactory(repo, this)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)
        binding.viewModel = noteViewModel

        val note = args.note
        noteViewModel.args.value = note

        binding.apply {
            noteInput.addTextChangedListener { noteViewModel.editText.value = it.toString() }
            noteInput.setText(noteViewModel.args.value?.note)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            noteViewModel.noteEvent.collect { event ->
                when(event) {
                    is NoteViewModel.NoteEvent.AddNote -> TODO()
                    is NoteViewModel.NoteEvent.EditNote -> TODO()
                    is NoteViewModel.NoteEvent.NavigateWithResult -> {
                        binding.noteInput.clearFocus()
                        setFragmentResult(
                            "add_edit_key",
                            bundleOf("add_edit_result" to event.result))
                        findNavController().popBackStack()
                    }
                    is NoteViewModel.NoteEvent.ShowInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is NoteViewModel.NoteEvent.NavigateToDeleteDialog -> TODO()
                }
            }
        }

        Log.d("ZZZZnote", "${noteViewModel.args.value}")
        Log.d("ZZZZarg", "${noteViewModel.stateArgs}")
    }

}