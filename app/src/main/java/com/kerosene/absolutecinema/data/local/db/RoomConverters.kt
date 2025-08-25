package com.kerosene.absolutecinema.data.local.db

import androidx.room.TypeConverter

class RoomConverters {
    @TypeConverter
    fun fromStringList(list: List<String>?): String {
        return list?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toStringList(data: String): List<String> {
        return if (data.isEmpty()) emptyList()
        else data.split(",")
    }
}