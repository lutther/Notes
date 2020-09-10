package com.example.notes.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.R
import com.example.notes.db.Note
import com.example.notes.repo.NoteRepo
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NoteViewModel(private val repo: NoteRepo): ViewModel(), Observable {

    val note = repo.notes

    @Bindable
    val inputNote = MutableLiveData<String>()

    fun saveOrUpdate() {
        val txt = inputNote.value!!
        insert(Note(0, txt))
    }

    fun insert(note: Note): Job = viewModelScope.launch {
            repo.insert(note)
        }

    fun  update(note: Note): Job = viewModelScope.launch {
        repo.update(note)
    }

    fun delete(note: Note): Job = viewModelScope.launch {
        repo.delete(note)
    }

    fun deleteAll(): Job = viewModelScope.launch {
        repo.deleteAll()
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
}