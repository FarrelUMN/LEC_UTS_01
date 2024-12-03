package com.example.lec_uts_01

import androidx.room.Query
import com.example.lec_uts_01.NoteDatabase
import com.example.lec_uts_01.Note

class NoteRepository(private val db: NoteDatabase) {
    suspend fun insertNote(note: Note) = db.getDaoNote().insertNote(note)
    suspend fun updateNote(note: Note) = db.getDaoNote().updateNote(note)
    suspend fun deleteNote(note: Note) = db.getDaoNote().deleteNote(note)

    fun getAllNote() = db.getDaoNote().getAllNotes()
    fun searchNote(query: String?) = db.getDaoNote().serachNote(query)
}
