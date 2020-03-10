package com.godelsoft.bestsemi_final

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import org.jetbrains.anko.startActivity

import com.google.android.material.bottomnavigation.BottomNavigationView
import org.jetbrains.anko.startActivityForResult


class MainActivity : AppCompatActivity() {
    private lateinit var floatingActionButton: View
    private var isFABActive = false

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
            R.id.itemSettings -> startActivity<MyAccountActivity>()
        }
        return true
    }

    fun showFAB() {
        if (!isFABActive) {
            isFABActive = true
            floatingActionButton = findViewById(R.id.floatingActionButton)
            floatingActionButton.visibility = View.VISIBLE
            floatingActionButton.startAnimation(
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.fab_full_size
                )
            )
        }
    }

    fun hideFAB() {
        floatingActionButton = findViewById(R.id.floatingActionButton)
        if (isFABActive) {
            isFABActive = false
            var anim = AnimationUtils.loadAnimation(this, R.anim.fab_null_size)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    floatingActionButton.visibility = View.GONE
                }
            })
            floatingActionButton.startAnimation(anim)
        }
    }

    @SuppressLint("CommitTransaction")
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }
    }
}