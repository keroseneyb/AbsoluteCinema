package com.kerosene.absolutecinema.domain.repository

import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.domain.entity.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    val notes: Flow<List<Note>>

    fun getNoteByMovieId(movieId: Int): Flow<Note>

    suspend fun createEmptyNote(movie: Movie)

    suspend fun updateNote(note: Note)

    suspend fun removeNoteByMovieId(movieId: Int)
}

