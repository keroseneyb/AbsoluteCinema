package com.kerosene.absolutecinema.domain.usecase

import com.kerosene.absolutecinema.domain.repository.NoteRepository
import javax.inject.Inject

class RemoveNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
) {

    suspend operator fun invoke(movieId: Int) = noteRepository.removeNoteByMovieId(movieId)
}