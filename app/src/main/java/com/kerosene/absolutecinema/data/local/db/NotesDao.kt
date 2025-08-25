package com.kerosene.absolutecinema.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kerosene.absolutecinema.data.local.model.NoteDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<NoteDbModel>>

    @Query("SELECT * FROM notes WHERE movieId = :movieId LIMIT 1")
    fun getNoteByMovieId(movieId: Int): Flow<NoteDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteDbModel)

    @Update
    suspend fun updateNote(note: NoteDbModel)

    @Query("DELETE FROM notes WHERE movieId = :movieId")
    suspend fun deleteNoteByMovieId(movieId: Int)
}