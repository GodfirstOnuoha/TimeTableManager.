package com.peacecodes.timetablemanager.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.peacecodes.timetablemanager.R
import com.peacecodes.timetablemanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        navigationView = binding.navView
        drawerLayout = binding.drawerLayout

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        //Connecting the drawer with navigation component
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        //Connecting toolbar with navController
        toolbar = binding.toolBar
        toolbar.setupWithNavController(navController, appBarConfiguration)

        //connect navigationView with navController
        navigationView.setupWithNavController(navController)

    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.fragment_container)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }
}