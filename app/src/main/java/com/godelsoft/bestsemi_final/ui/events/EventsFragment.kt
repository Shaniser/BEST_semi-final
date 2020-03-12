package com.godelsoft.bestsemi_final.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.godelsoft.bestsemi_final.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.startActivityForResult


class EventsFragment : Fragment() {

    private lateinit var eventsViewModel: EventsViewModel

    lateinit var recycleAdapter: EventAdapter
    lateinit var swipeContainer: SwipeRefreshLayout

    companion object {
        lateinit var homeFragment: EventsFragment
    }
    
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

        recyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy <= 0) {
                    if (activity is MainActivity) {
                        (activity as MainActivity).showFAB()
                        (activity as MainActivity).headerMain.text =
                            recyclerView.findChildViewUnder(0F, 0F)
                                ?.findViewById<TextView>(R.id.date)
                                ?.text
                    }
                } else {
                    if (activity is MainActivity) {
                        (activity as MainActivity).hideFAB()
                        (activity as MainActivity).headerMain.text =
                            recyclerView.findChildViewUnder(0F, 0F)
                                ?.findViewById<TextView>(R.id.date)
                                ?.text
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
                ))
        };

        // Инициализировать список событий
        if (EventsProvider.needsReload()) {
            reload()
        }
        else {
            recycleAdapter.update(EventsProvider.getAllAvailableEvents())
        }

        if (activity is MainActivity) {
           (activity as MainActivity).apply {
               headerMain.text = recyclerView.findChildViewUnder(0F, 0F)?.findViewById<TextView>(R.id.date)?.text
               showFAB()
               findViewById<View>(R.id.floatingActionButton).setOnClickListener {
                   startActivityForResult<CreateEventActivity>(1)
               }
           }
        }

        return root
    }
    
    fun reload() {
        swipeContainer.isRefreshing = true
        CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
            EventsProvider.reload()
            withContext(Dispatchers.Main) {
                recycleAdapter.update(EventsProvider.getAllAvailableEvents())
            }
            swipeContainer.isRefreshing = false
        }
    }
}