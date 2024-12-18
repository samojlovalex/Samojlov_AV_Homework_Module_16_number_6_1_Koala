package com.example.samojlov_av_homework_module_16_number_6_1_koala.utils.note

import androidx.lifecycle.LiveData
import com.example.samojlov_av_homework_module_16_number_6_1_koala.models.Note

class NoteRepository(
    private val noteDao: NoteDao,
) {

    val notes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    fun deleteAllNotes() {
        noteDao.deleteALLNotes()
    }

}