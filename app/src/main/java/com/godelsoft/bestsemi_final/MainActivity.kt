package com.godelsoft.bestsemi_final

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_chat))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        navView.setOnNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.navigation_home -> {
//                    // home
//                    Log.d("TAG", "0")
//                    true
//                }
//                R.id.navigation_dashboard -> {
//                    // dashboard
//                    Log.d("TAG", "1")
//                    true
//                }
//                R.id.navigation_chat -> {
//                    // chat
//                    Log.d("TAG", "2")
//                    true
//                }
//                else -> false
//            }
//        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemFilter -> {
                // TODO: открывать карточку с фильтрами событий (и чатов?)
            }
            R.id.itemSettings -> {
                // TODO: открывать окно настроек
            }
            R.id.itemNewEvent -> {
                // TODO: открывать окно создания события
            }
        }
        return true
    }
}