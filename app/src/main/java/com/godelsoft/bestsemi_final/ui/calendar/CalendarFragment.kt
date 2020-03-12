package com.godelsoft.bestsemi_final.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.godelsoft.bestsemi_final.MainActivity
import com.godelsoft.bestsemi_final.R
import com.godelsoft.bestsemi_final.StopableRecycleView
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.*
import kotlin.time.days

class CalendarFragment : Fragment() {

    private lateinit var calendarViewModel: CalendarViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        calendarViewModel =
                ViewModelProviders.of(this).get(CalendarViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_calendar, container, false)
        root.apply {
            val recycleView = findViewById<StopableRecycleView>(R.id.recycleView)
            val calendarView = findViewById<CalendarView>(R.id.calendarView)
            val currentDate = findViewById<TextView>(R.id.currentDate)


            recycleView.setScrollEnable(false)

            var c: Calendar = Calendar.getInstance()
            c.timeInMillis = calendarView.date

            var location = Calendar.getAvailableLocales()

            currentDate.text = "${c.get(Calendar.DAY_OF_MONTH)} ${c.getDisplayName(Calendar.MONTH, c.get(Calendar.MONTH), Locale("en", "RU"))} ${c.get(Calendar.YEAR)}"



            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                c.set(Calendar.MONTH, month)
                currentDate.text = "$dayOfMonth ${c.getDisplayName(Calendar.MONTH, 2, Locale("en", "RU"))} $year"
            }


        }

        if (activity is MainActivity) {
            (activity as MainActivity).hideFAB()
            (activity as MainActivity).headerMain.text = ""
        }




        return root
    }
}