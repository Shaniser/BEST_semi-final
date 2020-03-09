package com.godelsoft.bestsemi_final

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class CreateEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        val date = Calendar.getInstance()
        var isDateSetted = false
        var isTimeSetted = false

        dateButton.setOnClickListener {
            val c = Calendar.getInstance()
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    date.set(Calendar.YEAR, year)
                    date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    date.set(Calendar.MONTH, monthOfYear)
                    dateButton.text = with(date) { // TODO: Fix formatter
                        "${get(Calendar.DAY_OF_MONTH)}.${get(Calendar.MONTH)}.${get(Calendar.YEAR)}"
                    }
                    isDateSetted = true
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            )
            dpd.show()
        }

        timeButton.setOnClickListener {
            val c = Calendar.getInstance()
            val tpd = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, hours, minutes ->
                    date.set(Calendar.HOUR, hours)
                    date.set(Calendar.MINUTE, minutes)
                    timeButton.text = with(date) { // TODO: Fix formatter
                        "${get(Calendar.HOUR)}:${get(Calendar.MINUTE)}"
                    }
                    isTimeSetted = true
                },
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                true
            )
            tpd.show()
        }

        saveButton.setOnClickListener {
            if (!isDateSetted || !isTimeSetted) {
                Toast.makeText(
                    applicationContext,
                    "Set time and date first",
                    Toast.LENGTH_SHORT
                ).show();
            }
            else if (nameEdit.text.toString() == "" || bodyEdit.text.toString() == "") {
                Toast.makeText(
                    applicationContext,
                    "Set header and body first",
                    Toast.LENGTH_SHORT
                ).show();
            }
            else {
                CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
                    EventsProvider.addEvent(
                        Event(
                            1L, // TODO: Id
                            date,
                            "Sender", // TODO: Get name from firebase
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
}