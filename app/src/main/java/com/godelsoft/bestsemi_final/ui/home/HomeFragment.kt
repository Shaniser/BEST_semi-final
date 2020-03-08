package com.godelsoft.bestsemi_final.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.godelsoft.bestsemi_final.Auth
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.godelsoft.bestsemi_final.EventAdapter
import com.godelsoft.bestsemi_final.R
import kotlinx.android.synthetic.main.event_card.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

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
        recyclerView.adapter = EventAdapter(root.context)
        return root

//        homeViewModel.makeAuth("admin@ya.ru", "qwerty$i", fun(auth: Auth?, err: String?) {
//            activity?.runOnUiThread(fun() {
//                // Тут начинается код, выполняемый после завершения login
//                if (auth != null)
//                    Toast.makeText(
//                        context,
//                        "OK: ${auth.accessToken}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                else
//                    Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
//            })
//        })
    }
}