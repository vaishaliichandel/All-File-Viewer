package com.example.pdfreader.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.DrawableCompat
import com.example.pdfreader.BuildConfig
import com.example.pdfreader.R
import com.example.pdfreader.databinding.BottomShitDeleteBinding
import com.example.pdfreader.databinding.BottomShitDetailsBinding
import com.example.pdfreader.databinding.BottomShitMoreBinding
import com.example.pdfreader.databinding.BottonShitRenameBinding
import com.example.pdfreader.model.FileData
import com.example.pdfreader.utils.BottomSheetListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class BottomShitDialog : BottomSheetDialogFragment() {
    private lateinit var binding1: BottomShitMoreBinding
    private lateinit var fileData: FileData
    private var mListener: BottomSheetListener? = null
    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding1 = BottomShitMoreBinding.inflate(inflater, container, false)
        if (arguments?.containsKey("PDF_DATA") == true) {
            position = arguments?.getInt("POS")!!
            fileData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                (arguments?.getSerializable("PDF_DATA", FileData::class.java))!!
            } else {
                (arguments?.getSerializable("PDF_DATA") as FileData)
            }
            binding1.run {
                cbStar.isChecked = fileData.isFav
                dragHandle.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), fileData.color)
                setDrawableTintColor(
                    tvRename,
                    tvRename.compoundDrawablesRelative[0],
                    fileData.color
                )
                setDrawableTintColor(
                    tvDelete,
                    tvDelete.compoundDrawablesRelative[0],
                    fileData.color
                )
                setDrawableTintColor(
                    tvDetails,
                    tvDetails.compoundDrawablesRelative[0],
                    fileData.color
                )
                setDrawableTintColor(
                    tvShare,
                    tvShare.compoundDrawablesRelative[0],
                    fileData.color
                )
                tvFileName.text = fileData.pdfName
                tvFileLocation.text = fileData.absPath
                when (fileData.pdfExt) {
                    "pdf" -> ivPdfIcon.setImageResource(R.drawable.ic_pdf)
                    "docx", "doc" -> ivPdfIcon.setImageResource(R.drawable.ic_doc)
                    "xlsx", "xls" -> ivPdfIcon.setImageResource(R.drawable.ic_xls)
                    "ppt", "pptx" -> ivPdfIcon.setImageResource(R.drawable.ic_ppt)
                    "txt" -> ivPdfIcon.setImageResource(R.drawable.ic_txt)
                    "png" -> ivPdfIcon.setImageResource(R.drawable.ic_png)
                    "jpg" -> ivPdfIcon.setImageResource(R.drawable.ic_jpg)
                    "apk" -> ivPdfIcon.setImageResource(R.drawable.ic_apk)
                    "mp3" -> ivPdfIcon.setImageResource(R.drawable.ic_mp3)
                    "mp4" -> ivPdfIcon.setImageResource(R.drawable.ic_mp4)
                }
            }
        }
        return binding1.root
    }

    private fun setDrawableTintColor(textView: TextView, drawable: Drawable?, colorResId: Int) {
        if (drawable != null) {
            val wrappedDrawable = DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(
                wrappedDrawable,
                ContextCompat.getColor(requireContext(), colorResId)
            )
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                wrappedDrawable,
                null,
                null,
                null
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding1.run {
            mListener = activity as BottomSheetListener
            tvRename.setOnClickListener {
                val renameDialog = RenameDialog()
                renameDialog.show(parentFragmentManager, "RenameBottomSheet")
                val bundle = Bundle()
                bundle.putSerializable("FILE_DATA", fileData)
                bundle.putInt("FILE_POS", arguments?.getInt("POS") ?: 0)
                renameDialog.arguments = bundle
                dialog?.dismiss()
            }
            tvDetails.setOnClickListener {
                val detailsDialog = DetailsDialog()
                detailsDialog.show(parentFragmentManager, "DetailsBottomSheet")
                val bundle = Bundle()
                bundle.putSerializable("FILE_DATA", fileData)
                detailsDialog.arguments = bundle
                dialog?.dismiss()
            }
            tvDelete.setOnClickListener {
                val deleteDialog = DeleteDialog()
                deleteDialog.show(parentFragmentManager, "DetailsBottomSheet")
                val bundle = Bundle()
                bundle.putSerializable("FILE_DATA", fileData)
                bundle.putInt("FILE_POS", arguments?.getInt("POS") ?: 0)
                deleteDialog.arguments = bundle
                dialog?.dismiss()
            }
            tvShare.setOnClickListener {
                val pdfFile = File(fileData.absPath.toString())
                val uri: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    pdfFile
                )
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.setType("*/*")
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri)
                startActivity(Intent.createChooser(sharingIntent, "Share file using"))
            }
            cbStar.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    fileData.isFav = true
                    mListener?.onDataReceived(
                        isDelete = false,
                        isRename = false,
                        isFav = true,
                        fileData = fileData,
                        pos = position
                    )
                } else {
                    fileData.isFav = false
                    mListener?.onDataReceived(
                        isDelete = false,
                        isRename = false,
                        isFav = true,
                        fileData = fileData,
                        pos = position
                    )
                }
                dialog?.dismiss()
            }

        }
    }
}

class RenameDialog : BottomSheetDialogFragment() {
    private lateinit var binding1: BottonShitRenameBinding
    private var mListener: BottomSheetListener? = null
    private var fileData: FileData? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mListener = activity as BottomSheetListener
        binding1 = BottonShitRenameBinding.inflate(inflater, container, false)
        return binding1.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding1.run {
            dialog?.setCanceledOnTouchOutside(false)
            if (arguments?.containsKey("FILE_DATA") == true) {
                fileData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    (arguments?.getSerializable("FILE_DATA", FileData::class.java))!!
                else
                    (arguments?.getSerializable("FILE_DATA") as FileData)


                etFileName.setText(fileData?.pdfName?.substringBeforeLast("."))
                dragHandle.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), fileData!!.color)
                bDone.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), fileData!!.color)
            }
            bCancel.setOnClickListener {
                dialog?.dismiss()
            }
            bDone.setOnClickListener {
                val path: String? = File(fileData?.absPath.toString()).parentFile?.absolutePath
                val oldName = File(fileData?.absPath?.trimStart().toString())
                val newName = File(
                    path.plus("/").plus(
                        etFileName.text.toString().trimStart().plus(".").plus(fileData?.pdfExt)
                    )
                )
                oldName.renameTo(newName)
                fileData?.pdfName =
                    etFileName.text.toString().trimStart().plus(".").plus(fileData?.pdfExt)
                fileData?.absPath = newName.toString()

                mListener?.onDataReceived(
                    false,
                    isRename = true,
                    isFav = false,
                    fileData = fileData!!,
                    pos = arguments?.getInt("FILE_POS") ?: 0
                )
                dialog?.dismiss()
            }
        }
    }
}

class DetailsDialog : BottomSheetDialogFragment() {
    private lateinit var binding: BottomShitDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments?.containsKey("FILE_DATA") == true) {
            val fileData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                (arguments?.getSerializable("FILE_DATA", FileData::class.java))!!
            } else {
                (arguments?.getSerializable("FILE_DATA") as FileData)
            }
            binding.run {
                dragHandle.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), fileData.color)
                bDone.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), fileData.color)
                val formatter = SimpleDateFormat("hh:mm MMM d, yyyy", Locale.ENGLISH)
                tvNameValue.text = fileData.pdfName
                tvStorageValue.text = fileData.absPath
                tvModifyValue.text = fileData.pdfDate.let { formatter.format(it!!) }
                tvSizeValue.text = fileData.pdfSize
                bDone.setOnClickListener {
                    dialog?.dismiss()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomShitDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
}

class DeleteDialog : BottomSheetDialogFragment() {
    private lateinit var binding: BottomShitDeleteBinding
    private var mListener: BottomSheetListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListener = activity as BottomSheetListener

        if (arguments?.containsKey("FILE_DATA") == true) {
            val fileData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                (arguments?.getSerializable("FILE_DATA", FileData::class.java))!!
            } else {
                (arguments?.getSerializable("FILE_DATA") as FileData)
            }
            binding.run {
                dragHandle.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), fileData.color)
                bDone.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), fileData.color)
                bDone.setOnClickListener {
                    val fileName = File(fileData.absPath.toString())
                    fileName.delete()
                    mListener?.onDataReceived(
                        isDelete = true,
                        isRename = false,
                        isFav = false,
                        fileData = fileData,
                        pos = arguments?.getInt("FILE_POS") ?: 0
                    )
                    dialog?.dismiss()
                }
                bCancel.setOnClickListener {
                    dialog?.dismiss()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomShitDeleteBinding.inflate(inflater, container, false)
        return binding.root
    }
}
