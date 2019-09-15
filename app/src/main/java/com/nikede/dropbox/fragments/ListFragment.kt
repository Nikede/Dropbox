package com.nikede.dropbox.fragments

import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dropbox.core.DbxDownloader
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.FileMetadata
import com.dropbox.core.v2.files.ListFolderResult
import com.dropbox.core.v2.files.Metadata
import com.nikede.dropbox.R
import com.nikede.dropbox.activities.StartActivity
import com.nikede.dropbox.adapter.FileAdapter
import com.nikede.dropbox.adapter.FileAdapter.Callback
import com.nikede.dropbox.databinding.FragmentListBinding
import com.nikede.dropbox.models.File
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class ListFragment : Fragment() {

    var first = true

    var listData: ArrayList<Metadata> = ArrayList()
    val pathStack = Stack<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list, container, false
        )

        binding.list.layoutManager = LinearLayoutManager(activity)
        val a = activity as StartActivity
        a.supportActionBar?.title = "Dropbox"
        updateView("", binding.list)

        return binding.root
    }

    fun updateView(folder: String, view: RecyclerView) {
        CoroutineScope(Dispatchers.IO).async {
            listData.clear()
            listData.addAll(downloadData(folder).await())
            val config = DbxRequestConfig("dropbox/java-tutorial", "en_US")
            val sp = PreferenceManager.getDefaultSharedPreferences(activity)
            val client = DbxClientV2(config, sp.getString("Token", ""))
            val name = client.users().currentAccount.name.displayName
            launch(Dispatchers.Main) {
                sp.edit().putString("Name", name).apply()
                val a = activity as StartActivity
                a.supportActionBar?.subtitle = name
                view.adapter = FileAdapter(listData, context!!, object : Callback {
                    override fun onFolderClicked(item: Metadata) {
                        pathStack.add(item.pathLower)
                        updateView(item.pathLower, view)
                        val a = activity as StartActivity
                        a.supportActionBar?.title = item.name
                        first = false
                    }

                    override fun onFileClicked(item: FileMetadata) {
                        val file = File(
                            item.name,
                            item.size.toString(),
                            item.clientModified.toString(),
                            item.pathLower,
                            item.rev
                        )
                        val bundle = bundleOf("item" to file)
                        view.findNavController()
                            .navigate(R.id.action_listFragment_to_fileFragment, bundle)
                    }
                })
            }
        }
    }

    private fun downloadData(folder: String): Deferred<ArrayList<Metadata>> {
        return GlobalScope.async {
            val config = DbxRequestConfig("dropbox/java-tutorial", "en_US")
            val sp = PreferenceManager.getDefaultSharedPreferences(activity)
            val client = DbxClientV2(config, sp.getString("Token", ""))
            val entries = client.files().listFolder(folder).entries
            val list = ArrayList<Metadata>()
            for (entry in entries) {
                list.add(client.files().getMetadata(entry.pathLower))
            }
            list
        }
    }
}
