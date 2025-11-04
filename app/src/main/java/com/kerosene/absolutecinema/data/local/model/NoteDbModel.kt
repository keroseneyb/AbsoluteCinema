package com.kerosene.absolutecinema.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteDbModel(
    @PrimaryKey
    val noteId: Int,
    val title: String,
    val content: String,
)