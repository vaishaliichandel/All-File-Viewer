package com.example.pdfreader.utils

import android.view.View
import com.example.pdfreader.model.FileData

interface ClickListener {
    fun listItemClickListener(adapterPosition: Int, itemView: View, fileData: FileData)
    fun listItemOnMoreClickListener(adapterPosition: Int, itemView: View, fileData: FileData)
}