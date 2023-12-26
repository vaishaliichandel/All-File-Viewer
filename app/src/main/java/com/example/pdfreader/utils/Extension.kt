package com.example.pdfreader.utils

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import com.example.pdfreader.BuildConfig
import java.io.File


fun View.visible() {
    if (visibility != View.VISIBLE) visibility = View.VISIBLE
}

fun View.gone() {
    if (visibility != View.GONE) visibility = View.GONE
}

@SuppressLint("QueryPermissionsNeeded")
fun Context.openFile(localPath: String) {
    try {
        val pdfFile = File(localPath)
        val uri: Uri = FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID + ".provider",
            pdfFile
        )
        val intent = Intent(Intent.ACTION_VIEW)
        if (localPath.contains(".pdf")) {
            intent.setDataAndType(uri, "application/pdf")
        } else if (localPath.contains(".doc") || localPath.contains(".docx")) {
            intent.setDataAndType(uri, "application/msword")
        } else if (localPath.contains(".xls")) {
            intent.setDataAndType(uri, "application/vnd.ms-excel")
        } else if (localPath.contains(".xlsx")) {
            intent.setDataAndType(
                uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            )
        } else if (localPath.contains(".apk")) {
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
        } else if (localPath.contains(".png") || localPath
                .contains(".jpeg") || localPath.contains(".jpg")
        ) {
            intent.setDataAndType(uri, "image/*")
        } else if (localPath.contains(".mp4")) {
            intent.setDataAndType(uri, "video/mp4")
        } else if (localPath.contains(".mkv")) {
            intent.setDataAndType(uri, "video/x-matroska")
        } else if (localPath.contains(".txt")) {
            intent.setDataAndType(uri, "text/plain")
        } else if (localPath.contains(".pptx")) {
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
        } else if (localPath.contains(".ppt")) {
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            AppUtils.openPlayStore(this, localPath)
        }
//        startActivity(intent)
    } catch (e: Exception) {
        AppUtils.openPlayStore(this, localPath)
    } catch (e: ActivityNotFoundException) {
        Log.e("ActivityNotFoundException", "${e.message}")
    }
}
