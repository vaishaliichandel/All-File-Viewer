package com.example.pdfreader.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.pdfreader.R
import com.example.pdfreader.databinding.ActivityPermissionRequiredBinding
import com.example.pdfreader.room.FileDatabase
import com.example.pdfreader.room.repository.TaskRepository
import com.example.pdfreader.ui.fileList.MainActivity
import com.example.pdfreader.ui.fileList.MainViewModel
import com.example.pdfreader.utils.AppUtils
import com.example.pdfreader.utils.ViewModelFactory
import com.example.pdfreader.utils.gone
import com.example.pdfreader.utils.visible
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PermissionRequiredActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityPermissionRequiredBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var executor: ExecutorService
    private lateinit var handler: Handler

    companion object {
        private const val STORAGE_PERMISSION_CODE = 25
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionRequiredBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repository = TaskRepository(FileDatabase.getInstance(this).taskDao())
        val viewModelFactory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        executor = Executors.newSingleThreadExecutor()
        handler = Handler(Looper.getMainLooper())
        clickListener()
    }

    private fun clickListener() {
        binding.bContinue.setOnClickListener(this)
        binding.tvLater.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bContinue -> requestForStoragePermissions()
            R.id.tvLater -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun requestForStoragePermissions() {
        //Android is 11 (R) or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent()
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", this.packageName, null)
                intent.setData(uri)
                storageActivityResultLauncher.launch(intent)
            } catch (e: Exception) {
                val intent = Intent()
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                storageActivityResultLauncher.launch(intent)
            }
        } else {
            //Below android 11
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ), STORAGE_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty()) {
                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val read = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (read && write) {
                    binding.progressBar.visible()
                    binding.bContinue.isEnabled = false
                    binding.bContinue.alpha = 0.8F
                    executor.execute {
                        AppUtils.getFile(Environment.getExternalStorageDirectory())
//                        AppUtils.filePdfList.forEach {
                        viewModel.insertFile(AppUtils.filePdfList)
//                        }
                        handler.post {
                            binding.progressBar.gone()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    }
                } else
                    Toast.makeText(this, "Storage Permissions Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private
    val storageActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                //Android is 11 (R) or above
                if (Environment.isExternalStorageManager()) {
                    binding.progressBar.visible()
                    binding.bContinue.isEnabled = false
                    binding.bContinue.alpha = 0.8F
                    executor.execute {
                        AppUtils.getFile(Environment.getExternalStorageDirectory())
//                        AppUtils.filePdfList.forEach {
                        viewModel.insertFile(AppUtils.filePdfList)
//                        }
                        handler.post {
                            binding.progressBar.gone()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    }

                } else
                    Toast.makeText(this, "Storage Permissions Denied", Toast.LENGTH_SHORT).show()

            } else {
                //Below android 11
            }
        }

}