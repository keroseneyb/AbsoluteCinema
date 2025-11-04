package com.kerosene.absolutecinema.data.mapper

import com.kerosene.absolutecinema.data.local.model.NoteDbModel
import com.kerosene.absolutecinema.domain.entity.Note

fun NoteDbModel.noteToEntity() = Note(
    noteId = noteId,
    title = title,
    content = content
)

fun List<NoteDbModel>.noteToEntity(): List<Note> {
    return this.map { it.noteToEntity() }
}