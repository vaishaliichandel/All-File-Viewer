package com.example.pdfreader.utils

import com.example.pdfreader.model.FileData

interface BottomSheetListener {
    fun onDataReceived(
        isDelete: Boolean = false,
        isRename: Boolean = false,
        isFav: Boolean = false,
        fileData: FileData,
        pos: Int
    )
}