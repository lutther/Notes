package com.example.notes.ui

import androidx.databinding.Bindable
import androidx.databinding.Observable

import androidx.lifecycle.*
import com.example.notes.ADD_NOTE_RESULT
import com.example.notes.EDIT_NOTE_RESULT
import com.example.notes.db.Note
import com.example.notes.repo.NoteRepo
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NoteViewModel(private val repo: NoteRepo, val state: SavedStateHandle) : ViewModel(), Observable {

    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)

    val note: LiveData<List<Note>> = repo.notes(sortOrder.value).asLiveData()

    val stateArgs = state.getLiveData<Note>("note")

    val args = MutableLiveData<Note>()

    val editText = MutableLiveData<String>()

    private val noteEventChannel = Channel<NoteEvent>()
    val noteEvent = noteEventChannel.receiveAsFlow()


    fun saveOrUpdate() {
        if (editText.value.equals("")) {
            showInputMessage("Note cannot be blank")
            return
        }
        if (args.value != null) {
            val noteUpdate =
                args.value!!.copy(id = args.value!!.id, note = editText.value.toString())
            update(noteUpdate)
        } else {
            val newNote = Note(0, editText.value.toString())
            insert(newNote)
        }

    }

    private fun showInputMessage(s: String) = viewModelScope.launch {
        noteEventChannel.send(NoteEvent.ShowInputMessage(s))
    }

    fun insert(note: Note): Job = viewModelScope.launch {
        repo.insert(note)
        noteEventChannel.send(NoteEvent.NavigateWithResult(ADD_NOTE_RESULT))
    }

    fun update(note: Note): Job = viewModelScope.launch {
        repo.update(note)
        noteEventChannel.send(NoteEvent.NavigateWithResult(EDIT_NOTE_RESULT))
    }

    fun delete(note: Note): Job = viewModelScope.launch {
        repo.delete(note)
        showInputMessage("Note deleted")
    }

    fun deleteAll(): Job = viewModelScope.launch {
        repo.deleteAll()
        showInputMessage("All notes deleted")
    }

    fun searchNotes(searchQuery: String): LiveData<List<Note>> {
        return repo.searchNotes(searchQuery).asLiveData()
    }

    fun addNote() = viewModelScope.launch {
        noteEventChannel.send(NoteEvent.AddNote)
    }

    fun checkNoteAvailability(): Boolean {
        return note.value!!.isEmpty()
    }

    fun editNote(note: Note) = viewModelScope.launch {
        noteEventChannel.send(NoteEvent.EditNote(note))
    }

    fun editTextResult(result: Int) = viewModelScope.launch {
        when (result) {
            ADD_NOTE_RESULT -> noteEventChannel.send(NoteEvent.ShowInputMessage("Note added"))
            EDIT_NOTE_RESULT -> noteEventChannel.send(NoteEvent.ShowInputMessage("Note updated"))
        }
    }

    fun confirmDeleteDialog() = viewModelScope.launch {
        noteEventChannel.send(NoteEvent.NavigateToDeleteDialog)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    sealed class NoteEvent {
        object AddNote : NoteEvent()
        data class EditNote(val note: Note) : NoteEvent()
        data class NavigateWithResult(val result: Int) : NoteEvent()
        data class ShowInputMessage(val msg: String) : NoteEvent()
        object NavigateToDeleteDialog : NoteEvent()
    }
}

enum class SortOrder { BY_NAME, BY_DATE }