package com.godelsoft.bestsemi_final

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.godelsoft.bestsemi_final.model.Event
import com.godelsoft.bestsemi_final.util.CalFormatter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.coroutines.*
import java.util.*

class CreateEventActivity : AppCompatActivity() {
    var isSaving = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        val date = Calendar.getInstance()
        var isDateSetted = false
        var isTimeSetted = false

        back.setOnClickListener {
            onBackPressed()
        }

        dateButton.setOnClickListener {
            val c = Calendar.getInstance()
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    dateButton.text = CalFormatter.datef(date.also {
                        it.set(Calendar.YEAR, year)
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
                    timeButton.text = CalFormatter.timef(date.also {
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
            if (!isSaving) {
                if (!isDateSetted || !isTimeSetted) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.empty_event_date),
                        Toast.LENGTH_SHORT
                    ).show();
                } else if (nameEdit.text.toString().isBlank() || bodyEdit.text.toString().isBlank()) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.empty_event_body),
                        Toast.LENGTH_SHORT
                    ).show();
                } else {
                    isSaving = true
//                    resources.getString(R.id)
                    CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
                        EventsProvider.addEvent(
                            Event(
                                "1L", // TODO: Id
                                RawEvent(
                                    date.time,
                                    (FirebaseAuth.getInstance().currentUser?.displayName)
                                        ?: getString(R.string.unknown_user),
                                    nameEdit.text.toString(),
                                    bodyEdit.text.toString(),
                                    when {
                                        radioGlobal.isChecked -> EventCategory.GLOBAL
                                        radioPersonal.isChecked -> EventCategory.PERSONAL
                                        radioLBG.isChecked -> EventCategory.LBG
                                        else -> EventCategory.PERSONAL
                                    },
                                    null
                                )
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
}