package com.nikede.dropbox.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.file_list_item.view.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import com.dropbox.core.DbxDownloader
import com.dropbox.core.v2.files.FileMetadata
import com.dropbox.core.v2.files.FolderMetadata
import com.dropbox.core.v2.files.Metadata
import com.nikede.dropbox.R
import java.lang.Exception


class FileAdapter(val items: ArrayList<Metadata>, val context: Context, val callback: Callback) :
    RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        return FileViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.file_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(items[position])
    }

//    fun stringToBitMap(encodedString: String): Bitmap? {
//        try {
//            val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
//            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
//        } catch (e: Exception) {
//            e.message
//            return null
//        }
//
//    }

    inner class FileViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val preview = view.preview
        private val name = view.name
        private val info = view.info

        fun bind(item: Metadata) {
            try {
                val item = items.elementAt(position) as FileMetadata
                if (item.name.endsWith(".txt") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    preview.setImageDrawable(context.getDrawable(R.mipmap.ic_txt))
                name.text = item.name
                val infoT =
                    "${item.size}  ${item.clientModified}"
                info.text = infoT
                view.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) callback.onFileClicked(items[adapterPosition] as FileMetadata)
                }
            } catch (e: Exception) {
                try {
                    val item = items.elementAt(position) as FolderMetadata
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        preview.setImageDrawable(context.getDrawable(R.mipmap.ic_folder))
                    name.text = item.name
                    view.setOnClickListener {
                        if (adapterPosition != RecyclerView.NO_POSITION) callback.onFolderClicked(items[adapterPosition])
                    }
                } catch (e: Exception) {

                }
            }
        }
    }

    interface Callback {
        fun onFileClicked(item: FileMetadata)
        fun onFolderClicked(item: Metadata)
    }
}