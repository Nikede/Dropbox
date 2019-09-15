package com.nikede.dropbox.fragments


import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.nikede.dropbox.R
import com.nikede.dropbox.databinding.FragmentFileBinding
import com.nikede.dropbox.models.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.FileOutputStream
import android.os.Environment
import android.preference.PreferenceManager
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.v2.DbxClientV2


class FileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentFileBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_file, container, false
        )

        val file = arguments?.getSerializable("item") as File

        binding.name.text = file.name
        binding.size.text = file.size
        binding.format.text = file.format
        binding.path.text = file.path

        if (file.name.endsWith(".txt") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            binding.preview.setImageDrawable(context?.getDrawable(R.mipmap.ic_txt))

        return binding.root
    }
}
