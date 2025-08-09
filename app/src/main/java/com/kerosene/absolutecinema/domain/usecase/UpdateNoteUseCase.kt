package com.kerosene.absolutecinema.domain.usecase

import com.kerosene.absolutecinema.domain.entity.Note
import com.kerosene.absolutecinema.domain.repository.NoteRepository
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(note: Note) = repository.updateNote(note)
}