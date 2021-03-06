package com.godelsoft.bestsemi_final.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Filter
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.godelsoft.bestsemi_final.*
import com.godelsoft.bestsemi_final.model.User
import com.godelsoft.bestsemi_final.util.CalFormatter
import com.godelsoft.bestsemi_final.util.FirestoreUtil
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.startActivityForResult
import java.util.*


class EventsFragment : Fragment() {

    private lateinit var eventsViewModel: EventsViewModel
    private lateinit var currentUser: User
    private var isUserInited = false

    lateinit var recycleAdapter: EventAdapter
    lateinit var swipeContainer: SwipeRefreshLayout

    companion object {
        lateinit var homeFragment: EventsFragment
    }

    var curFilter = EventsFilter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        eventsViewModel =
            ViewModelProviders.of(this).get(EventsViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(root.context)
        recycleAdapter = EventAdapter(root.context)
        recyclerView.adapter = recycleAdapter
        swipeContainer = root.findViewById(R.id.swipeContainer)
        homeFragment = this

        swipeContainer.isRefreshing = true
        FirestoreUtil.getCurrentUser {
            currentUser = it
            isUserInited = true
            if (currentUser.role == Role.LBG)
                curFilter.showLBG = true
            if (swipeContainer.isRefreshing)
                swipeContainer.isRefreshing = false
        }

        recyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy <= 0) {
                    if (activity is MainActivity) {
                        (activity as MainActivity).showFAB()
                        val tdate = recyclerView.findChildViewUnder(0F, 0F)
                            ?.findViewById<TextView>(R.id.date)
                            ?.text
                            ?.split(".")
                        if (tdate != null) {
                            val c = Calendar.getInstance()
                            if (tdate.size == 3)
                                c.set(Calendar.YEAR, tdate[2].toInt() + 2000)
                            c.set(Calendar.MONTH, tdate[1].toInt() - 1)
                            c.set(Calendar.DAY_OF_MONTH, tdate[0].toInt())
                            val lang = if (context?.getResources()?.getString(R.string.back) == "Back") "en" else "ru"
                            (activity as MainActivity).headerMain.text =
                                "${c.get(Calendar.DAY_OF_MONTH)} ${c.getDisplayName(
                                    Calendar.MONTH, 2, Locale(lang, "RU")
                                )} ${c.get(Calendar.YEAR)}"
                        }
                    }
                } else {
                    if (activity is MainActivity) {
                        (activity as MainActivity).hideFAB()
                        val tdate = recyclerView.findChildViewUnder(0F, 0F)
                            ?.findViewById<TextView>(R.id.date)
                            ?.text
                            ?.split(".")
                        if (tdate != null) {
                            val c = Calendar.getInstance()
                            if (tdate.size == 3)
                                c.set(Calendar.YEAR, tdate[2].toInt() + 2000)
                            c.set(Calendar.MONTH, tdate[1].toInt() - 1)
                            c.set(Calendar.DAY_OF_MONTH, tdate[0].toInt())
                            val lang = if (context?.getResources()?.getString(R.string.back) == "Back") "en" else "ru"
                            (activity as MainActivity).headerMain.text =
                                "${c.get(Calendar.DAY_OF_MONTH)} ${c.getDisplayName(
                                    Calendar.MONTH, 2, Locale(lang, "RU")
                                )} ${c.get(Calendar.YEAR)}"
                        }
                    }
                }
            }
        })

        // Перезагрузка списка при свайпе вниз
        swipeContainer.setOnRefreshListener {
            reload()
        }
        if (container != null) {
            swipeContainer.setColorSchemeColors(
                ContextCompat.getColor(
                    container.context,
                    R.color.colorAccent
                )
            )
        };

        // Инициализировать список событий
        if (EventsProvider.needsReload()) {
            reload()
        } else {
            recycleAdapter.update(EventsProvider.getEventsByFilter {
                curFilter.checkCategory(it.event.category) &&
                        curFilter.checkDate(CalFormatter.getCalendarFromDate(it.event.date))
            })
        }

        if (activity is MainActivity) {
            (activity as MainActivity).apply {
                headerMain.text =
                    recyclerView.findChildViewUnder(0F, 0F)?.findViewById<TextView>(R.id.date)?.text
                showFAB()
                findViewById<View>(R.id.floatingActionButton).setOnClickListener {
                    startActivityForResult<CreateEventActivity>(1)
                }
            }
            var search = (activity as MainActivity)?.findViewById<ImageButton>(R.id.search)
            search.visibility = View.GONE
            var searchLine = (activity as MainActivity)?.findViewById<EditText>(R.id.nameSearch)
            searchLine.visibility = View.GONE
            var filter = (activity as MainActivity)?.findViewById<ImageButton>(R.id.filter)
            filter.visibility = View.VISIBLE
        }
        return root
    }

    fun reload() {
        swipeContainer.isRefreshing = true
        EventsProvider.reload {
            recycleAdapter.update(EventsProvider.getEventsByFilter {
                EventsFilter.filter.checkCategory(it.event.category) &&
                        EventsFilter.filter.checkDate(CalFormatter.getCalendarFromDate(it.event.date))
            })
            if (isUserInited)
                swipeContainer.isRefreshing = false
        }
    }

    fun applyFilter(f: EventsFilter?) {
        curFilter = f ?: EventsFilter()
        recycleAdapter.update(EventsProvider.getEventsByFilter {
            curFilter.checkCategory(it.event.category) &&
                    curFilter.checkDate(CalFormatter.getCalendarFromDate(it.event.date))
        })
    }
}