package com.kerosene.absolutecinema.domain.usecase

import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.domain.repository.FavouriteRepository
import com.kerosene.absolutecinema.domain.repository.NoteRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ToggleFavouriteStatusUseCase @Inject constructor(
    private val favouriteRepository: FavouriteRepository,
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(movie: Movie) {
        val isFavourite = favouriteRepository.observeIsFavourite(movie.id).first()

        if (isFavourite) {
            favouriteRepository.removeFromFavourite(movie.id)
            noteRepository.removeNoteByMovieId(movie.id)
        } else {
            favouriteRepository.addToFavourite(movie)
            noteRepository.createEmptyNote(movie)
        }
    }
}