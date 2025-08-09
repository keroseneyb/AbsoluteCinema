package com.kerosene.absolutecinema.domain.usecase

import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.domain.repository.NoteRepository
import javax.inject.Inject

class CreateEmptyNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(movie: Movie) = repository.createEmptyNote(movie)
}