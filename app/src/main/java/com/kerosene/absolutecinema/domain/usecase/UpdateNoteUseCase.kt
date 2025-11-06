package com.kerosene.absolutecinema.domain.usecase

import com.kerosene.absolutecinema.domain.repository.NoteRepository
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val repository: NoteRepository,
) {

    suspend operator fun invoke(noteId: Int, content: String) = repository.updateNoteContent(noteId, content)
}