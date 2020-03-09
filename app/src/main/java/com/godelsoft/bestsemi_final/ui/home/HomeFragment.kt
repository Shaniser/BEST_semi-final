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
import com.godelsoft.bestsemi_final.EventAdapter
import com.godelsoft.bestsemi_final.EventsProvider
import com.godelsoft.bestsemi_final.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
//        recyclerView.adapter = EventAdapter(root.context)

        homeViewModel.scope.launch(Dispatchers.IO) {
            // Объект auth уже должен быть готов тут
            val auth = Auth.login("admin@ya.ru", "qwerty1")
            if (auth.error == null)
                EventsProvider.init(auth)
            activity?.runOnUiThread(fun() {
                if (auth.error == null) {
                    // Тут recyclerView выводит обновлённые данные из адаптера
                    recyclerView.adapter = EventAdapter(root.context)
                }
                else {
                    Toast.makeText(
                        context,
                        "Error: ${auth.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        return root
    }
}