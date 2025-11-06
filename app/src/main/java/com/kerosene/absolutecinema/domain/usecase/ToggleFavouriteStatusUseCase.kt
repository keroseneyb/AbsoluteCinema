package com.kerosene.absolutecinema.domain.usecase

import com.kerosene.absolutecinema.domain.repository.FavouriteRepository
import com.kerosene.absolutecinema.domain.repository.NoteRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ToggleFavouriteStatusUseCase @Inject constructor(
    private val favouriteRepository: FavouriteRepository,
    private val noteRepository: NoteRepository,
) {
    suspend operator fun invoke(movieId: Int, title: String) {
        val isFavourite = favouriteRepository.observeIsFavourite(movieId).first()

        if (isFavourite) {
            favouriteRepository.removeFromFavourite(movieId)
            noteRepository.removeNoteByMovieId(movieId)
        } else {
            favouriteRepository.addToFavourite(movieId)
            noteRepository.createEmptyNote(movieId, title)
        }
    }
}