package com.example.pdfreader.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pdfreader.model.FileData
import com.example.pdfreader.room.dao.Dao

@Database(entities = [FileData::class], version = 1)
@TypeConverters(com.example.pdfreader.utils.Converters::class)

abstract class FileDatabase : RoomDatabase() {
    abstract fun taskDao(): Dao

    companion object {
        private var INSTANCE: FileDatabase? = null
        fun getInstance(context: Context): FileDatabase {
            if (INSTANCE == null) {
                synchronized(FileDatabase::class) {
                    INSTANCE = buildRoomDB(context)
                }
            }
            return INSTANCE!!
        }

        private const val DATABASE_NAME = "file_database"

        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                FileDatabase::class.java,
                DATABASE_NAME
            ).allowMainThreadQueries().build()
    }
}
