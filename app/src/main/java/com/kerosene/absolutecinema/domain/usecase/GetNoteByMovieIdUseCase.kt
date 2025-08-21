package com.kerosene.absolutecinema.domain.usecase

import com.kerosene.absolutecinema.domain.repository.NoteRepository
import javax.inject.Inject

class GetNoteByMovieIdUseCase @Inject constructor(
    private val repository: NoteRepository,
) {

    operator fun invoke(movieId: Int) = repository.getNoteByMovieId(movieId)
}