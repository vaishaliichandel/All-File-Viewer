package com.example.pdfreader.ui.fileList

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.pdfreader.R
import com.example.pdfreader.databinding.ActivityMainBinding
import com.example.pdfreader.model.FileData
import com.example.pdfreader.room.FileDatabase
import com.example.pdfreader.room.repository.TaskRepository
import com.example.pdfreader.ui.BottomShitDialog
import com.example.pdfreader.utils.AppUtils
import com.example.pdfreader.utils.BottomSheetListener
import com.example.pdfreader.utils.ClickListener
import com.example.pdfreader.utils.ViewModelFactory
import com.example.pdfreader.utils.gone
import com.example.pdfreader.utils.openFile
import com.example.pdfreader.utils.visible
import com.google.android.material.tabs.TabLayout
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity(), View.OnClickListener, BottomSheetListener {
    companion object {
        private const val STORAGE_PERMISSION_CODE = 23
    }

    private lateinit var executor: ExecutorService
    private lateinit var handler: Handler
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private var documentsList = ArrayList<FileData>()
    private var searchList = ArrayList<FileData>()
    private lateinit var mainAdapter: MainAdapter
    private var tabPosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        clickListener()
    }

    private fun observer() {
        viewModel.getAllFiles().observe(this) {
            binding.progressBar.gone()
            binding.tvSize.text = it.size.toString().plus(" Documents")
            mainAdapter.addAll(it.toSet())
            searchList.addAll(it.toSet())
            checkIfEmpty()
        }
    }

    private fun onTabSelected(searchList: List<FileData>, color: String) {
        binding.run {
            tvSize.text = if (searchList.size <= 1) searchList.toSet().size.toString()
                .plus(" Document") else searchList.toSet().size.toString().plus(" Documents")
            mainAdapter.addAll(searchList.filter { it.isFav }.toSet() + searchList.toSet())
            llLayout.setBackgroundColor(Color.parseColor(color))
            search.setBackgroundColor(Color.parseColor(color))
            tabLayout.setTabTextColors(R.color.text_gray, Color.parseColor(color))
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(color))
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.parseColor(color)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clickListener() {
        binding.bSet.setOnClickListener(this)
        binding.icSearch.setOnClickListener(this)
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                selectTab(tab)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })
        mainAdapter.setOnClickListener(object : ClickListener {
            override fun listItemClickListener(
                adapterPosition: Int, itemView: View, fileData: FileData
            ) {
                openFile(fileData.absPath!!)
            }

            override fun listItemOnMoreClickListener(
                adapterPosition: Int, itemView: View, fileData: FileData
            ) {
                val bottomShitDialog = BottomShitDialog()
                bottomShitDialog.show(supportFragmentManager, "ModalBottomSheet")
                val bundle = Bundle()
                bundle.putSerializable("PDF_DATA", fileData)
                bundle.putInt("POS", adapterPosition)
                bottomShitDialog.arguments = bundle
            }
        })
        binding.etSearch.setOnTouchListener(OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.etSearch.right - binding.etSearch.getCompoundDrawables()[2].getBounds()
                        .width()
                ) {
                    getFiles()
                    binding.etSearch.text?.clear()
                    binding.search.gone()
                    binding.icSearch.visible()
                    checkIfEmpty()
                    return@OnTouchListener true
                }
            }
            false
        })
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s!!.isEmpty()) {
                    getFiles()
                } else {
                    searchPdf(s.toString())
                }
            }
        })
    }

    private fun selectTab(tab: TabLayout.Tab?) {
        val text = tab?.customView as TextView?
        text?.setTypeface(null, Typeface.BOLD)
        binding.etSearch.text?.clear()
        tabPosition = tab?.position ?: 0
        when (tab?.position) {
            0 -> onTabSelected(searchList, "#062581")
            1 -> onTabSelected(searchList.filter { it.pdfExt == "pdf" }, "#C51C1C")
            2 -> onTabSelected(
                searchList.filter { it.pdfExt == "doc" || it.pdfExt == "docx" }, "#0084CD"
            )

            3 -> onTabSelected(
                searchList.filter { it.pdfExt == "xls" || it.pdfExt == "xlsx" }, "#008C6C"
            )

            4 -> onTabSelected(
                searchList.filter { it.pdfExt == "ppt" || it.pdfExt == "pptx" }, "#FF7A17"
            )

            5 -> onTabSelected(searchList.filter { it.pdfExt == "txt" }, "#6C6C6C")
            6 -> onTabSelected(searchList.filter { it.pdfExt == "apk" }, "#025A02")
            7 -> onTabSelected(searchList.filter { it.pdfExt == "jpg" }, "#062581")
            8 -> onTabSelected(searchList.filter { it.pdfExt == "png" }, "#A88800")
            9 -> onTabSelected(searchList.filter { it.pdfExt == "mp4" }, "#3F2C8E")
            10 -> onTabSelected(searchList.filter { it.pdfExt == "mp3" }, "#FF0066")
        }
        checkIfEmpty()
    }

    private fun searchPdf(text: String) {
        val searchList = searchList.filter { it.pdfName!!.contains(text, ignoreCase = true) }
        mainAdapter.addAll(searchList.toSet())
        when (tabPosition) {
            0 -> mainAdapter.addAll(searchList.toSet())
            1 -> mainAdapter.addAll(searchList.filter {
                it.pdfName!!.contains(
                    text, ignoreCase = true
                ) && it.pdfExt == "pdf"
            }.toSet())

            2 -> mainAdapter.addAll(searchList.filter {
                it.pdfName!!.contains(
                    text, ignoreCase = true
                ) && it.pdfExt == "doc" || it.pdfExt == "docx"
            }.toSet())

            3 -> mainAdapter.addAll(searchList.filter {
                it.pdfName!!.contains(
                    text, ignoreCase = true
                ) && it.pdfExt == "xls" || it.pdfExt == "xlsx"
            }.toSet())

            4 -> mainAdapter.addAll(searchList.filter {
                it.pdfName!!.contains(
                    text, ignoreCase = true
                ) && it.pdfExt == "ppt" || it.pdfExt == "pptx"
            }.toSet())

            5 -> mainAdapter.addAll(searchList.filter {
                it.pdfName!!.contains(
                    text, ignoreCase = true
                ) && it.pdfExt == "txt"
            }.toSet())

            6 -> mainAdapter.addAll(searchList.filter {
                it.pdfName!!.contains(
                    text, ignoreCase = true
                ) && it.pdfExt == "apk"
            }.toSet())

            7 -> mainAdapter.addAll(searchList.filter {
                it.pdfName!!.contains(
                    text, ignoreCase = true
                ) && it.pdfExt == "jpg"
            }.toSet())

            8 -> mainAdapter.addAll(searchList.filter {
                it.pdfName!!.contains(
                    text, ignoreCase = true
                ) && it.pdfExt == "png"
            }.toSet())

            9 -> mainAdapter.addAll(searchList.filter {
                it.pdfName!!.contains(
                    text, ignoreCase = true
                ) && it.pdfExt == "mp4"
            }.toSet())

            10 -> mainAdapter.addAll(searchList.filter {
                it.pdfName!!.contains(
                    text, ignoreCase = true
                ) && it.pdfExt == "mp3"
            }.toSet())
        }
        checkIfEmpty()
    }

    private fun initView() {
        val repository = TaskRepository(FileDatabase.getInstance(this).taskDao())
        val viewModelFactory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        mainAdapter = MainAdapter(documentsList)
        binding.rvDocuments.adapter = mainAdapter
        executor = Executors.newSingleThreadExecutor()
        handler = Handler(Looper.getMainLooper())
        if (checkStoragePermissions()) {
            binding.progressBar.visible()
            binding.llNoPermission.gone()
            binding.rvDocuments.visible()
        } else {
            binding.llNoPermission.visible()
            binding.rvDocuments.gone()
        }
        observer()
    }

    private fun checkIfEmpty() {
        binding.run {
            if (documentsList.isEmpty() && checkStoragePermissions()) {
                var fileName = " "
                when (tabPosition) {
                    0 -> fileName = "Documents"
                    1 -> fileName = "PDF File"
                    2 -> fileName = "Documents File"
                    3 -> fileName = "Excel File"
                    4 -> fileName = "PPT File"
                    5 -> fileName = "Text File"
                    6 -> fileName = "APK File"
                    7 -> fileName = "JPG File"
                    8 -> fileName = "PNG File"
                    9 -> fileName = "MP4 File"
                    10 -> fileName = "MP3 File"
                }
                llNoPermission.visible()
                imgPermission.setImageResource(R.drawable.ic_no_data)
                tvPermission.text = getString(R.string.no_file)
                tvDescription.text = getString(R.string.no_found, fileName)
                bSet.gone()
                rvDocuments.gone()
                tvSize.gone()
            } else if (!checkStoragePermissions()) {
                llNoPermission.visible()
                bSet.visible()
                tvSize.gone()
                imgPermission.setImageResource(R.drawable.img_request)
                tvPermission.text = getString(R.string.no_permission)
                tvDescription.text = getString(R.string.permission_is_required_to_access_all_files)
            } else {
                rvDocuments.visible()
                tvSize.visible()
                llNoPermission.gone()
            }
        }
    }

    private fun checkStoragePermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //Android is 11 (R) or above
            Environment.isExternalStorageManager()
        } else {
            //Below android 11
            val write = ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val read = ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED
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
                    executor.execute {
                        AppUtils.getFile(Environment.getExternalStorageDirectory())
                        viewModel.insertFile(AppUtils.filePdfList)
                    }
                } else {
                    Toast.makeText(this, "Storage Permissions Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val storageActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                //Android is 11 (R) or above
                if (Environment.isExternalStorageManager()) {
                    binding.progressBar.visible()
                    executor.execute {
                        AppUtils.getFile(Environment.getExternalStorageDirectory())
                        viewModel.insertFile(AppUtils.filePdfList)
                    }
                } else {
                    Toast.makeText(this, "Storage Permissions Denied", Toast.LENGTH_SHORT).show()
                }
            } else {
                //Below android 11
            }
        }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bSet -> requestForStoragePermissions()
            R.id.icSearch -> {
                binding.search.visible()
                binding.icSearch.gone()
                binding.search.requestFocus()
            }
        }
    }

    override fun onDataReceived(
        isDelete: Boolean, isRename: Boolean, isFav: Boolean, fileData: FileData, pos: Int
    ) {
        Log.e("fileData", "$pos $fileData")
        if (isRename) {
            viewModel.updateFile(fileData)
            documentsList[pos] = fileData
            mainAdapter.notifyItemChanged(pos)
        } else if (isDelete) {
            viewModel.deleteFile(fileData)
            searchList.remove(fileData)
            documentsList.remove(fileData)
            mainAdapter.notifyItemRemoved(pos)
        } else {
            viewModel.updateFile(fileData)
            documentsList[pos] = fileData
            mainAdapter.notifyItemChanged(pos)
        }
    }

    private fun getFiles() {
        when (tabPosition) {
            0 -> mainAdapter.addAll(searchList.toSet())
            1 -> mainAdapter.addAll(searchList.filter { it.pdfExt == "pdf" }.toSet())
            2 -> mainAdapter.addAll(searchList.filter { it.pdfExt == "doc" || it.pdfExt == "docx" }
                .toSet())

            3 -> mainAdapter.addAll(searchList.filter { it.pdfExt == "xls" || it.pdfExt == "xlsx" }
                .toSet())

            4 -> mainAdapter.addAll(searchList.filter { it.pdfExt == "ppt" || it.pdfExt == "pptx" }
                .toSet())

            5 -> mainAdapter.addAll(searchList.filter { it.pdfExt == "txt" }.toSet())
            6 -> mainAdapter.addAll(searchList.filter { it.pdfExt == "apk" }.toSet())
            7 -> mainAdapter.addAll(searchList.filter { it.pdfExt == "jpg" }.toSet())
            8 -> mainAdapter.addAll(searchList.filter { it.pdfExt == "png" }.toSet())
            9 -> mainAdapter.addAll(searchList.filter { it.pdfExt == "mp4" }.toSet())
            10 -> mainAdapter.addAll(searchList.filter { it.pdfExt == "mp3" }.toSet())
        }
    }
}