package com.example.notes.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.databinding.FragmentNoteListBinding
import com.example.notes.db.Note
import com.example.notes.db.NoteDatabase
import com.example.notes.repo.NoteRepo
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect

class NotesList : Fragment(R.layout.fragment_note_list), RecyclerViewAdapter.OnItemClickListener,
    SearchView.OnQueryTextListener {

    lateinit var noteViewModel: NoteViewModel
    val recyclerView = RecyclerViewAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentNoteListBinding.bind(view)

        val linearLayout = LinearLayoutManager(activity)

        val dao = NoteDatabase.getInstance(activity?.application).noteDao
        val repo = NoteRepo(dao)
        val factory = NoteViewModelFactory(repo, this)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)

        binding.viewModel = noteViewModel
        binding.lifecycleOwner = this

        Log.d("ZZZZsort", "${noteViewModel.sortOrder.value}")

        noteViewModel.note.observe(viewLifecycleOwner, Observer {
            binding.apply {
                noteRecyclerView.apply {
                    layoutManager = linearLayout
                    adapter = recyclerView
                    (noteRecyclerView.adapter as RecyclerViewAdapter).submitList(it)
                    setHasFixedSize(true)

                    ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                        0,
                        ItemTouchHelper.LEFT
                    ) {
                        override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
                        ): Boolean {
                            return false
                        }

                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                            val note = recyclerView.currentList[viewHolder.adapterPosition]
                            noteViewModel.delete(note)
                            (adapter)?.notifyDataSetChanged()
                        }

                    }).attachToRecyclerView(binding.noteRecyclerView)

                }

                fab.setOnClickListener {
                    noteViewModel.addNote()
                }

            }

        })

        setFragmentResultListener("add_edit_key") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            noteViewModel.editTextResult(result)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            noteViewModel.noteEvent.collect { event ->
                when (event) {
                    is NoteViewModel.NoteEvent.AddNote -> {
                        val d = NotesListDirections.actionFragmentNotesListToWriteNote2()
                        findNavController().navigate(d)
                    }
                    is NoteViewModel.NoteEvent.EditNote -> {
                        val d = NotesListDirections.actionFragmentNotesListToWriteNote2(event.note)
                        findNavController().navigate(d)
                    }
                    is NoteViewModel.NoteEvent.ShowInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is NoteViewModel.NoteEvent.NavigateWithResult -> TODO()
                    is NoteViewModel.NoteEvent.NavigateToDeleteDialog -> {
                        ConfirmDeleteDialog().show(childFragmentManager, ConfirmDeleteDialog.TAG)
                    }
                }
            }
        }
        setHasOptionsMenu(true)
    }

    override fun onItemClick(note: Note) {
        noteViewModel.editNote(note)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                noteViewModel.confirmDeleteDialog()
                true
            }
            R.id.sort_by_date -> {
                noteViewModel.sortOrder.value = SortOrder.BY_DATE
                true
            }
            R.id.sort_by_name -> {
                noteViewModel.sortOrder.value = SortOrder.BY_NAME
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        val search = menu.findItem(R.id.search)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            s(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            s(query)
        }
        return true
    }

    private fun s(query: String) {
        val searchQuery = "%$query%"
        noteViewModel.searchNotes(searchQuery).observe(this, { list ->
            list.let {
                recyclerView.submitList(it)
            }
        })
    }

}