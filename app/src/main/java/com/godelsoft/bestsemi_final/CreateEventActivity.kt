package com.godelsoft.bestsemi_final

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.coroutines.*
import java.util.*

class CreateEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        dateButton.setOnClickListener {
            // TODO: Picker
        }

        timeButton.setOnClickListener {
            // TODO: Picker
        }

        saveButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
                EventsProvider.addEvent(
                    Event(
                        1L,                         // TODO: Id
                        Date(2020, 3, 10, 8, 30),   // TODO: Date
                        "Sender",                   // TODO: Get name from firebase
                        nameEdit.text.toString(),
                        bodyEdit.text.toString(),
                        when {
                            radioGlobal.isChecked -> EventCategory.GLOBAL
                            radioPersonal.isChecked -> EventCategory.PERSONAL
                            radioLGB.isChecked -> EventCategory.LGB
                            else -> throw Exception("Wrong event type")
                        },
                        null
                    )
                )
                withContext(Dispatchers.Main) {
                    setResult(1)
                    finish()
                }
            }
        }
    }
}