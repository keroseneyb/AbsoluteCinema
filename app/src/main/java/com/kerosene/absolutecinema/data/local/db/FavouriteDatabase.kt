package com.kerosene.absolutecinema.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kerosene.absolutecinema.data.local.model.MovieDbModel
import com.kerosene.absolutecinema.data.local.model.NoteDbModel

@Database(entities = [MovieDbModel::class, NoteDbModel::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class FavouriteDatabase : RoomDatabase() {

    abstract fun favouriteMoviesDao(): FavouriteMoviesDao

    abstract fun notesDao(): NotesDao
}