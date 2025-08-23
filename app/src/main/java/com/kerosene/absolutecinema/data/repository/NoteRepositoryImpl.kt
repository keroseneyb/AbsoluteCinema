package com.kerosene.absolutecinema.data.repository

import com.kerosene.absolutecinema.data.local.db.NotesDao
import com.kerosene.absolutecinema.data.mapper.noteToDbModel
import com.kerosene.absolutecinema.data.mapper.noteToEntity
import com.kerosene.absolutecinema.domain.entity.Movie
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

    override suspend fun createEmptyNote(movie: Movie) {
        val newNote = Note(
            movieId = movie.id,
            title = movie.name ?: "",
            content = "",
            id = movie.id
        )
        notesDao.insertNote(newNote.noteToDbModel())
    }

    override suspend fun updateNote(note: Note) {
        notesDao.updateNote(note.noteToDbModel())
    }

    override suspend fun removeNoteByMovieId(movieId: Int) {
        notesDao.deleteNoteByMovieId(movieId)
    }
}