package com.godelsoft.bestsemi_final.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.godelsoft.bestsemi_final.*
import com.godelsoft.bestsemi_final.ui.events.EventsFragment
import com.godelsoft.bestsemi_final.ui.events.EventsViewModel
import com.godelsoft.bestsemi_final.util.CalFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CalendarFragment : Fragment() {

    lateinit var recycleAdapter: EventAdapter
    private lateinit var calendarView: CalendarView

    companion object {
        lateinit var calendarFragment: CalendarFragment
    }

    var curFilter = EventsFilter()
    var curSelectedDate = Calendar.getInstance()

    private lateinit var calendarViewModel: CalendarViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        calendarViewModel =
                ViewModelProviders.of(this).get(CalendarViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_calendar, container, false)
        calendarFragment = this
        root.apply {
            val recycleView = findViewById<StopableRecycleView>(R.id.recycleView)
            calendarView = findViewById<CalendarView>(R.id.calendarView)
            val currentDate = findViewById<TextView>(R.id.currentDate)

            recycleView.layoutManager = LinearLayoutManager(root.context)
            recycleAdapter = EventAdapter(root.context)
            recycleView.adapter = recycleAdapter

            if (root is MotionLayout) {
                root.setTransitionListener(object : MotionLayout.TransitionListener {
                    override fun onTransitionStarted(
                        motionLayout: MotionLayout,
                        i: Int,
                        i1: Int
                    ) {
                        ViewCompat.setElevation((activity as MainActivity).headerConLay, 6F)
                    }

                    override fun onTransitionChange(
                        motionLayout: MotionLayout,
                        i: Int,
                        i1: Int,
                        v: Float
                    ) {
                    }

                    override fun onTransitionCompleted(motionLayout: MotionLayout, i: Int) {
                        if (motionLayout.currentState == R.id.end) {
                            recycleView.setScrollEnable(true)
                            ViewCompat.setElevation((activity as MainActivity).headerConLay, 0F)
                        } else {
                            ViewCompat.setElevation((activity as MainActivity).headerConLay, 6F)
                        }

                    }
                    override fun onTransitionTrigger(
                        motionLayout: MotionLayout,
                        i: Int,
                        b: Boolean,
                        v: Float
                    ) {
                    }
                })
            }

            recycleView.setScrollEnable(false)

            var c: Calendar = Calendar.getInstance()
            c.timeInMillis = calendarView.date
            setDate()

            var location = Calendar.getAvailableLocales()

            currentDate.text = "${c.get(Calendar.DAY_OF_MONTH)} ${c.getDisplayName(Calendar.MONTH, 2, Locale("en", "RU"))} ${c.get(Calendar.YEAR)}"

            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                c.set(Calendar.MONTH, month)
                c.set(Calendar.YEAR, year)
                setDate(c)
                currentDate.text = "$dayOfMonth ${c.getDisplayName(Calendar.MONTH, 2, Locale("en", "RU"))} $year"
            }
        }

        // Инициализировать список событий
        if (EventsProvider.needsReload()) {
            reload()
        }
        else {
            recycleAdapter.update(EventsProvider.getEventsByFilter {
                EventsFilter().also { ef ->
                    ef.dateType = EventsFilterDateType.DATE
                    ef.filterDate = curSelectedDate
                }.checkDate(CalFormatter.getCalendarFromDate(it.event.date)) &&
                        curFilter.checkCategory(it.event.category)
            })
        }

        if (activity is MainActivity) {
            (activity as MainActivity).hideFAB()
            (activity as MainActivity).headerMain.text = ""
        }
        return root
    }

    fun reload() {
        EventsProvider.reload {
            recycleAdapter.update(EventsProvider.getEventsByFilter {
                EventsFilter().also { ef ->
                    ef.dateType = EventsFilterDateType.DATE
                    ef.filterDate = Calendar.getInstance()
                }.checkDate(CalFormatter.getCalendarFromDate(it.event.date)) &&
                        EventsFilter.filter.checkCategory(it.event.category)
            })
        }
    }

    fun setDate(calendar: Calendar) {
        curSelectedDate = calendar
        recycleAdapter.update(EventsProvider.getEventsByFilter {
            EventsFilter().also { ef ->
                ef.dateType = EventsFilterDateType.DATE
                ef.filterDate = calendar
            }.checkDate(CalFormatter.getCalendarFromDate(it.event.date)) &&
                    curFilter.checkCategory(it.event.category)
        })
    }

    fun setDate() {
        val c: Calendar = Calendar.getInstance()
        c.timeInMillis = calendarView.date
        setDate(c)
    }

    override fun onResume() {
        setDate()
        super.onResume()
    }

    fun applyFilter(f: EventsFilter?) {
        curFilter = f ?: EventsFilter()
        recycleAdapter.update(EventsProvider.getEventsByFilter {
            EventsFilter().also { ef ->
                ef.dateType = EventsFilterDateType.DATE
                ef.filterDate = curSelectedDate
            }.checkDate(CalFormatter.getCalendarFromDate(it.event.date)) &&
                    curFilter.checkCategory(it.event.category)
        })
    }
}