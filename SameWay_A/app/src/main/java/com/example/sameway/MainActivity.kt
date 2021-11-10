package com.example.sameway

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.sameway.databinding.ActivityMainBinding
import com.example.sameway.general.BottomNavManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavManager: BottomNavManager
    @Inject
    lateinit var bottomNavManagerFactory: BottomNavManager.BottomNavManagerFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //------------------------------------

        bottomNavManager = bottomNavManagerFactory.create(supportFragmentManager, binding.bottomNavigationView)
        setupNavigationManager()
        binding.bottomNavigationView.selectedItemId = R.id.nav2

    }
    private fun setupNavigationManager()
    {
        bottomNavManager.setupNavController()
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        bottomNavManager.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle)
    {
        super.onRestoreInstanceState(savedInstanceState)
        bottomNavManager.onRestoreInstanceState(savedInstanceState)
        setupNavigationManager()
    }

    override fun onBackPressed()
    {
        if(!bottomNavManager.onBackPressed()) super.onBackPressed()
    }

}