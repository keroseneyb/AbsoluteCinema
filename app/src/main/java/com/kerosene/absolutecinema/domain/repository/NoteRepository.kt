package com.kerosene.absolutecinema.domain.repository

import com.kerosene.absolutecinema.domain.entity.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    val notes: Flow<List<Note>>

    fun getNoteByMovieId(movieId: Int): Flow<Note>

    suspend fun createEmptyNote(movieId: Int, title: String)

    suspend fun updateNoteContent(noteId: Int, content: String)

    suspend fun removeNoteByMovieId(movieId: Int)
}

