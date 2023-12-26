package com.example.pdfreader.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pdfreader.model.FileData

@androidx.room.Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FileData>)

    @Insert
    fun insertFile(taskModel: FileData)

    @Update
    fun updateFile(taskModel: FileData)

    @Delete
    fun deleteFile(taskModel: FileData)

    @Query("SELECT * from file_table")
    fun getAllFiles(): LiveData<List<FileData>>
}