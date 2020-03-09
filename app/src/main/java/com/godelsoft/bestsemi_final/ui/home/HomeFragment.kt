package com.godelsoft.bestsemi_final.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.godelsoft.bestsemi_final.Auth
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.godelsoft.bestsemi_final.EventAdapter
import com.godelsoft.bestsemi_final.EventsProvider
import com.godelsoft.bestsemi_final.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        return root
    }
}