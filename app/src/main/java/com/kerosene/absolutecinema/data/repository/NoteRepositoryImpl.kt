package com.kerosene.absolutecinema.data.repository

import com.kerosene.absolutecinema.data.local.db.NotesDao
import com.kerosene.absolutecinema.data.local.model.NoteDbModel
import com.kerosene.absolutecinema.data.mapper.noteToEntity
import com.kerosene.absolutecinema.domain.entity.Note
import com.kerosene.absolutecinema.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val notesDao: NotesDao,
) : NoteRepository {

    override val notes: Flow<List<Note>> = notesDao
        .getAllNotes()
        .map { it.noteToEntity() }

    override fun getNoteByMovieId(movieId: Int): Flow<Note> {
        return notesDao
            .getNoteByMovieId(movieId)
            .map { it.noteToEntity() }
    }

    override suspend fun createEmptyNote(movieId: Int, title: String) {
        val newNote = NoteDbModel(
            noteId = movieId,
            title = title,
            content = ""
        )
        notesDao.insertNote(newNote)
    }

    override suspend fun updateNoteContent(noteId: Int, content: String) {
        notesDao.updateNoteContent(noteId, content)
    }

    override suspend fun removeNoteByMovieId(movieId: Int) {
        notesDao.deleteNoteByMovieId(movieId)
    }
}