package com.godelsoft.bestsemi_final

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.onRefresh


class MainActivity : AppCompatActivity() {
    private lateinit var floatingActionButton: View
    private var isFABActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        findViewById<View>(R.id.container).apply {
            filter.setOnClickListener {
                //TODO ФИЛЬТЕР СДЕЛАЙТЕ УЖЕ ПОЖАЛУЙСТА >_<
            }
            account.setOnClickListener{
                startActivity<MyAccountActivity>()
            }
        }
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
            val anim = AnimationUtils.loadAnimation(this, R.anim.fab_null_size)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1 -> {
                    val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeContainer)
                    swipeRefreshLayout?.post(Runnable {
                        swipeRefreshLayout.isRefreshing = true
                    })
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
