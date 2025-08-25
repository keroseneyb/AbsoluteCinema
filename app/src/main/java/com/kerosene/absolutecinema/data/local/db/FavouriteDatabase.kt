package com.kerosene.absolutecinema.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kerosene.absolutecinema.data.local.model.MovieDbModel
import com.kerosene.absolutecinema.data.local.model.NoteDbModel

@Database(entities = [MovieDbModel::class, NoteDbModel::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class FavouriteDatabase : RoomDatabase() {

    abstract fun favouriteMoviesDao(): FavouriteMoviesDao

    abstract fun notesDao(): NotesDao

    companion object {

        private const val DB_NAME = "FavouriteDatabase"
        private var INSTANCE: FavouriteDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): FavouriteDatabase {
            INSTANCE?.let { return it }

            synchronized(LOCK) {
                INSTANCE?.let { return it }
                val database = Room.databaseBuilder(
                    context = context,
                    klass = FavouriteDatabase::class.java,
                    name = DB_NAME
                ).build()
                INSTANCE = database
                return database
            }
        }
    }
}