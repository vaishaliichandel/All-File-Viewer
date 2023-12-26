package com.example.pdfreader.ui.fileList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdfreader.model.FileData
import com.example.pdfreader.room.repository.TaskRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: TaskRepository
) : ViewModel() {
    fun getAllFiles(): LiveData<List<FileData>> {
        return userRepository.getAllFiles()
    }

    fun insertFile(fileList: List<FileData>) {
        viewModelScope.launch {
            userRepository.insertData(fileList)
        }
    }

    fun updateFile(user: FileData) {
        viewModelScope.launch {
            userRepository.updateFile(user)
        }
    }

    fun deleteFile(user: FileData) {
        viewModelScope.launch {
            userRepository.deleteFile(user)
        }
    }

}