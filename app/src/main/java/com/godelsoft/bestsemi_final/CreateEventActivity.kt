package com.godelsoft.bestsemi_final

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.event_card.*
import kotlinx.android.synthetic.main.item_text_message.*
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
                    dateButton.text = EventsProvider.formatDate(date.also {
                        it.set(Calendar.YEAR, year)
                        it.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        it.set(Calendar.MONTH, monthOfYear)
                    })
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
                    timeButton.text = EventsProvider.formatTime(date.also {
                        it.set(Calendar.HOUR_OF_DAY, hours)
                        it.set(Calendar.MINUTE, minutes)
                    })
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
                            (FirebaseAuth.getInstance().currentUser?.displayName) ?: "Unknown", // TODO: Unknown?
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
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
            }
        }
    }
}