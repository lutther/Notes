package com.example.notes

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes.databinding.ActivityMainBinding
import com.example.notes.db.NoteDao
import com.example.notes.db.NoteDatabase
import com.example.notes.repo.NoteRepo
import com.example.notes.ui.NoteViewModel
import com.example.notes.ui.NoteViewModelFactory
import com.example.notes.ui.RecyclerViewAdapter
import com.example.notes.ui.WriteNote
import kotlinx.android.synthetic.main.list_items.*

abstract class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteViewModel: NoteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        val dao: NoteDao = NoteDatabase.getInstance(application).noteDao
        val repo = NoteRepo(dao)
        val factory = NoteViewModelFactory(repo)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)
        binding.viewModel = noteViewModel
        binding.lifecycleOwner = this

//        val writeNote = WriteNote()
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.fragementHolder, writeNote)
//        }

        recyclerView()
    }

    private fun recyclerView() {
        binding.noteRecyclerView.layoutManager = LinearLayoutManager(this)
        noteList()
    }

    private fun noteList() {
        noteViewModel.note.observe(this, Observer {
            binding.noteRecyclerView.adapter = RecyclerViewAdapter(it)
        })
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.new_note) {
//            openWr
//        }
//        return super.onOptionsItemSelected(item)
//    }

}
