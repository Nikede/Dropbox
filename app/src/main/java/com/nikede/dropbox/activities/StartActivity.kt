package com.nikede.dropbox.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.nikede.dropbox.R
import androidx.databinding.DataBindingUtil
import androidx.navigation.ui.NavigationUI
import com.nikede.dropbox.databinding.ActivityStartBinding


class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityStartBinding>(this, R.layout.activity_start)
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
        supportActionBar?.title = "Dropbox"
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }
}
