package com.example.notes.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.databinding.ListItemsBinding
import com.example.notes.db.Note

class RecyclerViewAdapter(private val noteList: List<Note>): RecyclerView.Adapter<NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemsBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_items, parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(noteList[position])
    }
}

class NoteViewHolder(val binding: ListItemsBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(note: Note) {
        binding.noteTextView.text = note.note
    }
}