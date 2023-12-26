package com.example.pdfreader.ui.fileList

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.pdfreader.R
import com.example.pdfreader.databinding.ListItemPdfBinding
import com.example.pdfreader.model.FileData
import com.example.pdfreader.utils.ClickListener
import java.text.SimpleDateFormat
import java.util.Locale

class MainAdapter(private val documentsList: ArrayList<FileData>) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    private var clickListener: ClickListener? = null
    class MainViewHolder(private val binding: ListItemPdfBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(fileData: FileData, clickListener: ClickListener?) {
            val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            binding.tvPdfName.text = fileData.pdfName
            binding.tvParentName.text = fileData.directoryName
            binding.tvPdfDate.text = fileData.pdfDate.let { formatter.format(it!!) }
            binding.tvPdfSize.text = fileData.pdfSize
            binding.ivStar.isVisible = fileData.isFav
            when (fileData.pdfExt) {
                "pdf" -> binding.ivPdfIcon.setImageResource(R.drawable.ic_pdf)
                "docx", "doc" -> binding.ivPdfIcon.setImageResource(R.drawable.ic_doc)
                "xlsx", "xls" -> binding.ivPdfIcon.setImageResource(R.drawable.ic_xls)
                "ppt", "pptx" -> binding.ivPdfIcon.setImageResource(R.drawable.ic_ppt)
                "txt" -> binding.ivPdfIcon.setImageResource(R.drawable.ic_txt)
                "png" -> binding.ivPdfIcon.setImageResource(R.drawable.ic_png)
                "jpg" -> binding.ivPdfIcon.setImageResource(R.drawable.ic_jpg)
                "apk" -> binding.ivPdfIcon.setImageResource(R.drawable.ic_apk)
                "mp3" -> binding.ivPdfIcon.setImageResource(R.drawable.ic_mp3)
                "mp4" -> binding.ivPdfIcon.setImageResource(R.drawable.ic_mp4)
            }
            binding.root.setOnClickListener {
                clickListener?.listItemClickListener(adapterPosition, itemView, fileData)
            }
            binding.icMore.setOnClickListener {
                clickListener?.listItemOnMoreClickListener(adapterPosition, itemView, fileData)
            }
        }
    }

    fun setOnClickListener(onClickListener: ClickListener) {
        this.clickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ListItemPdfBinding.inflate(inflater, parent, false)
        return MainViewHolder(view)
    }

    override fun getItemCount() = documentsList.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(documentsList[position], clickListener)
    }

    fun addAll(arrayList: Set<FileData>) {
        documentsList.clear()
        documentsList.addAll(arrayList)
        Handler(Looper.getMainLooper()).post { notifyDataSetChanged() }
    }

}