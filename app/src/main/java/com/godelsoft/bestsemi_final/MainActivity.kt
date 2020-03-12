package com.godelsoft.bestsemi_final

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.godelsoft.bestsemi_final.glide.GlideApp
import com.godelsoft.bestsemi_final.model.User
import com.godelsoft.bestsemi_final.ui.calendar.CalendarFragment
import com.godelsoft.bestsemi_final.ui.chat.ChatFragment
import com.godelsoft.bestsemi_final.ui.events.EventsFragment
import com.godelsoft.bestsemi_final.util.CalFormatter
import com.godelsoft.bestsemi_final.util.FirestoreUtil
import com.godelsoft.bestsemi_final.util.StorageUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var floatingActionButton: View
    private var isFABActive = false
    lateinit var headerConLay: ConstraintLayout
    lateinit var headerMain: TextView
    private lateinit var currentUser: User

    companion object {
        lateinit var mainActivity: MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivity = this

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        FirestoreUtil.getCurrentUser {user ->
            currentUser = user
            if (currentUser.role == Role.LBG) {
                checkBoxLBG.isEnabled = true
                checkBoxLBG.isChecked = true
                applyFilters(Calendar.getInstance())
            }
            if (user.profilePicture != null)
                GlideApp.with(this)
                    .load(StorageUtil.pathToReference(user.profilePicture))
                    .placeholder(R.drawable.ic_account_circle_black_24dp)
                    .into(imageView_profile)
        }

        findViewById<View>(R.id.container).apply {
            popUp.visibility = View.GONE
            headerMain = header
            headerConLay = headerConLayout

            filter.setOnClickListener {
                popUp.visibility = View.VISIBLE
            }
            account.setOnClickListener {
                startActivityForResult<MyAccountActivity>(121)
            }
            nameSearch.visibility = View.GONE
            nameSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    val sstr = nameSearch.text.toString()
                    ChatFragment.chatFragment.extUpdateRecycler(sstr)
                }
            })
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
                radioFuture.isChecked -> EventsFilterDateType.FUTURE
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

    fun updatePhoto() {
        FirestoreUtil.getCurrentUser { user ->
            if (user.profilePicture != null)
                GlideApp.with(this)
                    .load(StorageUtil.pathToReference(user.profilePicture))
                    .placeholder(R.drawable.ic_account_circle_36px)
                    .into(imageView_profile)
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
