package com.example.pdfreader.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.pdfreader.R
import com.example.pdfreader.model.FileData
import java.io.File
import java.text.DecimalFormat
import java.util.Date

class AppUtils {
    companion object {
        private var filePdf: ArrayList<File> = ArrayList()
        var filePdfList: ArrayList<FileData> = ArrayList()
        fun getFile(dir: File): ArrayList<File> {
            val listFile = dir.listFiles()
            if (listFile != null && listFile.isNotEmpty()) {
                for (i in listFile.indices) {
                    if (listFile[i].isDirectory) {
                        getFile(listFile[i])
                    } else {
                        if (listFile[i].name.endsWith(".pdf")) {
                            filePdf.add(listFile[i])
                            filePdfList.add(
                                FileData(
                                    pdfName = listFile[i].name,
                                    pdfDate = Date(listFile[i].lastModified()),
                                    pdfTime = 0.0.toLong(),
                                    pdfSize = getFileSize(listFile[i].length()),
                                    pdfRealSize = listFile[i].length(),
                                    pdfExt = "pdf",
                                    absPath = listFile[i].absolutePath,
                                    directoryName = listFile[i].parentFile?.name,
                                    color = R.color.pdf
                                )
                            )
                        }
                        if (listFile[i].name.endsWith(".txt")) {
                            filePdf.add(listFile[i])
                            filePdfList.add(
                                FileData(
                                    pdfName = listFile[i].name,
                                    pdfDate = Date(listFile[i].lastModified()),
                                    pdfTime = 0.0.toLong(),
                                    pdfSize = getFileSize(listFile[i].length()),
                                    pdfRealSize = listFile[i].length(),
                                    pdfExt = "txt",
                                    absPath = listFile[i].absolutePath,
                                    directoryName = listFile[i].parentFile?.name,
                                    color = R.color.txt
                                )
                            )
                        }
                        if (listFile[i].name.endsWith(".apk")) {
                            filePdf.add(listFile[i])
                            filePdfList.add(
                                FileData(
                                    pdfName = listFile[i].name,
                                    pdfDate = Date(listFile[i].lastModified()),
                                    pdfTime = 0.0.toLong(),
                                    pdfSize = getFileSize(listFile[i].length()),
                                    pdfRealSize = listFile[i].length(),
                                    pdfExt = "apk",
                                    absPath = listFile[i].absolutePath,
                                    directoryName = listFile[i].parentFile?.name,
                                    color = R.color.apk
                                )
                            )
                        }
                        if (listFile[i].name.endsWith(".doc")) {
                            filePdf.add(listFile[i])
                            filePdfList.add(
                                FileData(
                                    pdfName = listFile[i].name,
                                    pdfDate = Date(listFile[i].lastModified()),
                                    pdfTime = 0.0.toLong(),
                                    pdfSize = getFileSize(listFile[i].length()),
                                    pdfRealSize = listFile[i].length(),
                                    pdfExt = "doc",
                                    absPath = listFile[i].absolutePath,
                                    directoryName = listFile[i].parentFile?.name,
                                    color = R.color.doc
                                )
                            )
                        }
                        if (listFile[i].name.endsWith(".docx")) {
                            filePdf.add(listFile[i])
                            filePdfList.add(
                                FileData(
                                    pdfName = listFile[i].name,
                                    pdfDate = Date(listFile[i].lastModified()),
                                    pdfTime = 0.0.toLong(),
                                    pdfSize = getFileSize(listFile[i].length()),
                                    pdfRealSize = listFile[i].length(),
                                    pdfExt = "docx",
                                    absPath = listFile[i].absolutePath,
                                    directoryName = listFile[i].parentFile?.name,
                                    color = R.color.doc
                                )
                            )
                        }
                        if (listFile[i].name.endsWith(".xlsx")) {
                            filePdf.add(listFile[i])
                            filePdfList.add(
                                FileData(
                                    pdfName = listFile[i].name,
                                    pdfDate = Date(listFile[i].lastModified()),
                                    pdfTime = 0.0.toLong(),
                                    pdfSize = getFileSize(listFile[i].length()),
                                    pdfRealSize = listFile[i].length(),
                                    pdfExt = "xlsx",
                                    absPath = listFile[i].absolutePath,
                                    directoryName = listFile[i].parentFile?.name,
                                    color = R.color.excel
                                )
                            )
                        }
                        if (listFile[i].name.endsWith(".xls")) {
                            filePdf.add(listFile[i])
                            filePdfList.add(
                                FileData(
                                    pdfName = listFile[i].name,
                                    pdfDate = Date(listFile[i].lastModified()),
                                    pdfTime = 0.0.toLong(),
                                    pdfSize = getFileSize(listFile[i].length()),
                                    pdfRealSize = listFile[i].length(),
                                    pdfExt = "xls",
                                    absPath = listFile[i].absolutePath,
                                    directoryName = listFile[i].parentFile?.name,
                                    color = R.color.excel
                                )
                            )
                        }
                        if (listFile[i].name.endsWith(".ppt")) {
                            filePdf.add(listFile[i])
                            filePdfList.add(
                                FileData(
                                    pdfName = listFile[i].name,
                                    pdfDate = Date(listFile[i].lastModified()),
                                    pdfTime = 0.0.toLong(),
                                    pdfSize = getFileSize(listFile[i].length()),
                                    pdfRealSize = listFile[i].length(),
                                    pdfExt = "ppt",
                                    absPath = listFile[i].absolutePath,
                                    directoryName = listFile[i].parentFile?.name,
                                    color = R.color.ppt
                                )
                            )
                        }
                        if (listFile[i].name.endsWith(".pptx")) {
                            filePdf.add(listFile[i])
                            filePdfList.add(
                                FileData(
                                    pdfName = listFile[i].name,
                                    pdfDate = Date(listFile[i].lastModified()),
                                    pdfTime = 0.0.toLong(),
                                    pdfSize = getFileSize(listFile[i].length()),
                                    pdfRealSize = listFile[i].length(),
                                    pdfExt = "pptx",
                                    absPath = listFile[i].absolutePath,
                                    directoryName = listFile[i].parentFile?.name,
                                    color = R.color.ppt
                                )
                            )
                        }
                        if (listFile[i].name.endsWith(".png")) {
                            filePdf.add(listFile[i])
                            filePdfList.add(
                                FileData(
                                    pdfName = listFile[i].name,
                                    pdfDate = Date(listFile[i].lastModified()),
                                    pdfTime = 0.0.toLong(),
                                    pdfSize = getFileSize(listFile[i].length()),
                                    pdfRealSize = listFile[i].length(),
                                    pdfExt = "png",
                                    absPath = listFile[i].absolutePath,
                                    directoryName = listFile[i].parentFile?.name,
                                    color = R.color.png
                                )
                            )
                        }
                        if (listFile[i].name.endsWith(".jpg")) {
                            filePdf.add(listFile[i])
                            filePdfList.add(
                                FileData(
                                    pdfName = listFile[i].name,
                                    pdfDate = Date(listFile[i].lastModified()),
                                    pdfTime = 0.0.toLong(),
                                    pdfSize = getFileSize(listFile[i].length()),
                                    pdfRealSize = listFile[i].length(),
                                    pdfExt = "jpg",
                                    absPath = listFile[i].absolutePath,
                                    directoryName = listFile[i].parentFile?.name,
                                    color = R.color.jpg
                                )
                            )
                        }
                        if (listFile[i].name.endsWith(".mp3")) {
                            filePdf.add(listFile[i])
                            filePdfList.add(
                                FileData(
                                    pdfName = listFile[i].name,
                                    pdfDate = Date(listFile[i].lastModified()),
                                    pdfTime = 0.0.toLong(),
                                    pdfSize = getFileSize(listFile[i].length()),
                                    pdfRealSize = listFile[i].length(),
                                    pdfExt = "mp3",
                                    absPath = listFile[i].absolutePath,
                                    directoryName = listFile[i].parentFile?.name,
                                    color = R.color.mp3
                                )
                            )
                        }
                        if (listFile[i].name.endsWith(".mp4")) {
                            filePdf.add(listFile[i])
                            filePdfList.add(
                                FileData(
                                    pdfName = listFile[i].name,
                                    pdfDate = Date(listFile[i].lastModified()),
                                    pdfTime = 0.0.toLong(),
                                    pdfSize = getFileSize(listFile[i].length()),
                                    pdfRealSize = listFile[i].length(),
                                    pdfExt = "mp4",
                                    absPath = listFile[i].absolutePath,
                                    directoryName = listFile[i].parentFile?.name,
                                    color = R.color.mp4
                                )
                            )
                        }
                    }
                }
            }
            return filePdf
        }

        private fun getFileSize(length: Long): String {
            val hrSize: String?
            val b = length.toDouble()
            val k = length / 1024.0
            val m = length / 1024.0 / 1024.0
            val g = length / 1024.0 / 1024.0 / 1024.0
            val t = length / 1024.0 / 1024.0 / 1024.0 / 1024.0
            val dec = DecimalFormat("0")
            hrSize = if (t > 1) {
                dec.format(t).plus(" TB")
            } else if (g > 1) {
                dec.format(g).plus(" GB")
            } else if (m > 1) {
                dec.format(m).plus(" MB")
            } else if (k > 1) {
                dec.format(k).plus(" KB")
            } else {
                dec.format(b).plus(" Bytes")
            }
            return hrSize
        }

        fun openPlayStore(context: Context, localPath: String) {
            Toast.makeText(
                context,
                "You may not have a proper app for viewing this content",
                Toast.LENGTH_SHORT
            ).show()
            var intent = Intent(Intent.ACTION_VIEW)
            if (localPath.contains(".pptx")) {
                intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.microsoft.office.powerpoint")
                )
            } else if (localPath.contains(".pdf")) {
                intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.adobe.reader")
                )
            } else if (localPath.contains(".doc") || localPath.contains(".docx")) {
                intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.microsoft.office.word")
                )
            } else if (localPath.contains(".xls") || localPath.contains(".xlsx")) {
                intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.microsoft.office.excel")
                )
            }

            try {
                context.startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                if (localPath.contains(".pptx")) {
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.microsoft.office.powerpoint"))
                }
                if (localPath.contains(".doc") || localPath.contains(".docx")) {
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.microsoft.office.word"))

                }
                if (localPath.contains(".xls") || localPath.contains(".xlsx")) {
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.microsoft.office.excel"))
                }
                if (localPath.contains(".xls") || localPath.contains(".xlsx")) {
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.adobe.reader"))
                }
                context.startActivity(intent)
            }
        }
    }
}