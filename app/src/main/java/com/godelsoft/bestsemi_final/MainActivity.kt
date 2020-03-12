package com.godelsoft.bestsemi_final

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.godelsoft.bestsemi_final.model.User
import com.godelsoft.bestsemi_final.ui.calendar.CalendarFragment
import com.godelsoft.bestsemi_final.ui.events.EventsFragment
import com.godelsoft.bestsemi_final.util.CalFormatter
import com.godelsoft.bestsemi_final.util.FirestoreUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_create_event.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_main.view.back
import org.jetbrains.anko.startActivity
import java.lang.Exception
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var floatingActionButton: View
    private var isFABActive = false
    lateinit var headerConLay: ConstraintLayout
    lateinit var headerMain: TextView
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        FirestoreUtil.getCurrentUser {
            currentUser = it
            if (currentUser.role == Role.LBG) {
                checkBoxLBG.isEnabled = true
                checkBoxLBG.isChecked = true
                applyFilters(Calendar.getInstance())
            }
        }

        findViewById<View>(R.id.container).apply {
            popUp.visibility = View.GONE
            headerMain = header
            headerConLay = headerConLayout

            filter.setOnClickListener {
//                if (currentUser.role != Role.LBG)
//                    radioLBG.isEnabled = false
                popUp.visibility = View.VISIBLE
            }
            account.setOnClickListener {
                startActivity<MyAccountActivity>()
            }
            search.visibility = View.GONE
            search.setOnClickListener{
                //TODO - поиск
            }
        }

        radioGroupFilterDate.setOnCheckedChangeListener { _, _ ->
            buttonChooseDate.isEnabled = radioSelectDay.isChecked
        }
        val choosedDate = Calendar.getInstance()
        buttonChooseDate.text = CalFormatter.datef(Calendar.getInstance())
        buttonChooseDate.setOnClickListener {
            val c = Calendar.getInstance()
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    buttonChooseDate.text = CalFormatter.datef(choosedDate.also {
                        it.set(Calendar.YEAR, year)
                        it.set(Calendar.YEAR, year)
                        it.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        it.set(Calendar.MONTH, monthOfYear)
                    })
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            )
            dpd.show()
        }

        popUp.apply {
            back.setOnClickListener {
                popUp.visibility = View.GONE
            }
            apply.setOnClickListener {
                applyFilters(choosedDate)
                popUp.visibility = View.GONE
            }
        }
    }

    fun applyFilters(choosedDate: Calendar) {
        val f = EventsFilter().also {
            it.showLBG = checkBoxLBG.isChecked
            it.showGlobal = checkBoxGlobal.isChecked
            it.showPersonal = checkBoxPersonal.isChecked
            it.dateType = when {
                radioAllDays.isChecked -> EventsFilterDateType.ALL
                radioToday.isChecked -> EventsFilterDateType.TODAY
                radioWeek.isChecked -> EventsFilterDateType.WEEK
                radioSelectDay.isChecked -> EventsFilterDateType.DATE
                else -> EventsFilterDateType.ALL
            }
            it.filterDate = choosedDate
        }
        EventsFilter.filter = f
        EventsFragment.homeFragment.applyFilter(f)
        try {
            CalendarFragment.calendarFragment.applyFilter(f)
        } catch (e: Exception) {} // Рукалицо...
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
                    EventsFragment.homeFragment.reload()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
