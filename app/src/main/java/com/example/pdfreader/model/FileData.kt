package com.example.pdfreader.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date

@Entity(tableName = "file_table")
data class FileData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo var pdfName: String? = null,
    @ColumnInfo val pdfDate: Date? = null,
    @ColumnInfo var pdfTime: Long? = null,
    @ColumnInfo val pdfSize: String? = null,
    @ColumnInfo val pdfRealSize: Long? = null,
    @ColumnInfo val pdfExt: String? = null,
    @ColumnInfo var absPath: String? = null,
    @ColumnInfo var isFav: Boolean = false,
    @ColumnInfo var directoryName: String? = null,
    @ColumnInfo var color: Int,
) : Serializable