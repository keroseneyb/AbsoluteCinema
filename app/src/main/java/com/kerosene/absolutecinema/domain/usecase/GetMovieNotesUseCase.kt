package com.kerosene.absolutecinema.domain.usecase

import com.kerosene.absolutecinema.domain.repository.NoteRepository
import javax.inject.Inject

class GetMovieNotesUseCase @Inject constructor(
    private val repository: NoteRepository,
) {

    operator fun invoke() = repository.notes
}