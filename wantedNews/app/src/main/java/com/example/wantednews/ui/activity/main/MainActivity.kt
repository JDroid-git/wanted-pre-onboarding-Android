package com.example.wantednews.ui.activity.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.wantednews.R
import com.example.wantednews.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initLayout()
    }

    private fun initLayout() {
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_bottom_top_news, R.id.nav_bottom_categories, R.id.nav_bottom_saved))

        setupActionBarWithNavController(navController!!, appBarConfiguration)
        binding.navView.setupWithNavController(navController!!)
        binding.navView.setOnItemReselectedListener { navController!!.popBackStack() }
    }

    override fun onSupportNavigateUp(): Boolean {
        navController?.popBackStack()
        return super.onSupportNavigateUp()
    }
}