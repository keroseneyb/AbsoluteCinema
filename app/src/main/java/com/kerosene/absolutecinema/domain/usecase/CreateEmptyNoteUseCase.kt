package com.kerosene.absolutecinema.domain.usecase

import com.kerosene.absolutecinema.domain.repository.NoteRepository
import javax.inject.Inject

class CreateEmptyNoteUseCase @Inject constructor(
    private val repository: NoteRepository,
) {

    suspend operator fun invoke(movieId: Int, title: String) = repository.createEmptyNote(movieId, title)
}