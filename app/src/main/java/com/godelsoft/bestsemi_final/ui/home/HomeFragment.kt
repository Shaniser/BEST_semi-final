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
import com.godelsoft.bestsemi_final.R
import kotlinx.android.synthetic.main.event_card.view.*

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
        val sLinLay: LinearLayout = root.findViewById(R.id.SLinLay)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        for (i in 0..10) {
            var card: View = layoutInflater.inflate(R.layout.event_card, null);
            card.textView.text = "Button $i"
            card.setOnClickListener {
                // Пример авторизации:
                // Валидный пароль: "qwerty1" то есть на кнопке "button 1"
                // Задержка авторизаци - 3 секунды
                Auth.login("admin@ya.ru", "qwerty$i", fun (res: Auth?, err: String) {
                    activity?.runOnUiThread(fun () {
                        // Тут начинается код, выполняемый после завершения login
                        if (res != null)
                            Toast.makeText(context, "OK: ${res.accessToken}", Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                    })
                })
            }
            sLinLay.addView(card)
        }
        return root
    }
}