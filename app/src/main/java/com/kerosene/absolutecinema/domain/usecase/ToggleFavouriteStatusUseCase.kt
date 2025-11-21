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
        } else {
            favouriteRepository.addToFavourite(movieId)
            val existingNote = try {
                noteRepository.getNoteByMovieId(movieId).first()
                true
            } catch (e: Exception) {
                false
            }
            if (!existingNote) {
                noteRepository.createEmptyNote(movieId, title)
            }
        }
    }
}