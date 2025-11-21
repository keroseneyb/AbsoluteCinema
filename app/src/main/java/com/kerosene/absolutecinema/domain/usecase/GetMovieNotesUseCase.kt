package com.kerosene.absolutecinema.domain.usecase

import com.kerosene.absolutecinema.domain.entity.Note
import com.kerosene.absolutecinema.domain.repository.FavouriteRepository
import com.kerosene.absolutecinema.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetMovieNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
    private val favouriteRepository: FavouriteRepository,
) {

    operator fun invoke(): Flow<List<Note>> {
        return combine(
            noteRepository.notes,
            favouriteRepository.favouriteMovies
        ) { allNotes, favouriteMovies ->
            val favouriteMovieIds = favouriteMovies.map { it.id }.toSet()
            allNotes.filter { note -> note.noteId in favouriteMovieIds }
        }
    }
}