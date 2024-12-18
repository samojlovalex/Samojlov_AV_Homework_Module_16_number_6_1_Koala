package com.example.samojlov_av_homework_module_16_number_6_1_koala.utils.note

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.samojlov_av_homework_module_16_number_6_1_koala.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository
    val notes: LiveData<List<Note>>

    init {
        val dao = NoteDataBase.getDataBase(application).getNoteDao()
        repository = NoteRepository(dao)
        notes = repository.notes
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNote(note)
    }

    fun insertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertNote(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateNote(note)
    }

    fun deleteAllNote() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllNotes()
    }

}