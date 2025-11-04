package com.kerosene.absolutecinema.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kerosene.absolutecinema.data.local.model.NoteDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<NoteDbModel>>

    @Query("SELECT * FROM notes WHERE noteId = :movieId LIMIT 1")
    fun getNoteByMovieId(movieId: Int): Flow<NoteDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteDbModel)

    @Query("UPDATE notes SET content = :content WHERE noteId = :noteId")
    suspend fun updateNoteContent(noteId: Int, content: String)

    @Query("DELETE FROM notes WHERE noteId = :noteId")
    suspend fun deleteNoteByMovieId(noteId: Int)
}