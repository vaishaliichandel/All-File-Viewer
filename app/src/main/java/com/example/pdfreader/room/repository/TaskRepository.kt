package com.example.pdfreader.room.repository

import androidx.lifecycle.LiveData
import com.example.pdfreader.model.FileData
import com.example.pdfreader.room.dao.Dao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskRepository(private val taskDao: Dao) {
    fun insertFile(task: FileData) = taskDao.insertFile(task)
    fun updateFile(task: FileData) = taskDao.updateFile(task)
    fun deleteFile(task: FileData) = taskDao.deleteFile(task)
    fun getAllFiles(): LiveData<List<FileData>> {
        return taskDao.getAllFiles()
    }

    suspend fun insertData(data: List<FileData>) {
        withContext(Dispatchers.IO) {
            taskDao.insertAll(data)
        }
    }
}