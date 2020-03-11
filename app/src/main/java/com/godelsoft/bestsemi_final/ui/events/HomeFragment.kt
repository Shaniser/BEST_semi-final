package com.godelsoft.bestsemi_final.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(root.context)
        val recyclerAdapter = EventAdapter(root.context)
        recyclerView.adapter = recyclerAdapter
        val swipeContainer: SwipeRefreshLayout = root.findViewById(R.id.swipeContainer)

        recyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy <= 0) {
                    if (activity is MainActivity) {
                        (activity as MainActivity).showFAB()
                    }
                } else {
                    if (activity is MainActivity) {
                        (activity as MainActivity).hideFAB()
                    }
                }
            }
        })

        // Перезагрузка списка при свайпе вниз
        swipeContainer.setOnRefreshListener {
            CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
                EventsProvider.reload()
                withContext(Dispatchers.Main) {
                    recyclerAdapter.update(EventsProvider.getAllAvaiableEvents())
                }
                swipeContainer.isRefreshing = false
            }
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
            swipeContainer.isRefreshing = true
            CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
                EventsProvider.reload()
                withContext(Dispatchers.Main) {
                    recyclerAdapter.update(EventsProvider.getAllAvaiableEvents())
                    swipeContainer.isRefreshing = false
                }
            }
        }
        else {
            recyclerAdapter.update(EventsProvider.getAllAvaiableEvents())
        }

        if (activity is MainActivity) {
           (activity as MainActivity).apply {
               showFAB()
               findViewById<View>(R.id.floatingActionButton).setOnClickListener {
                   startActivityForResult<CreateEventActivity>(1)
               }
           }
        }

        return root
    }
}