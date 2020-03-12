package com.godelsoft.bestsemi_final.util

import android.util.Log
import com.godelsoft.bestsemi_final.RawEvent
import com.godelsoft.bestsemi_final.model.Event
import com.google.firebase.firestore.FirebaseFirestore


object EventUtil {
    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun getEvents(onComplete: (events: MutableList<Event>) -> Unit) {
        firestoreInstance.collection("events")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "Events listener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<Event>()
                querySnapshot!!.documents.forEach {
                    items.add(Event(it.id, it.toObject(RawEvent::class.java)!!))
                }
                onComplete(items)
            }
    }

    fun addEvent(event: RawEvent) {
        firestoreInstance.collection("events")
            .add(event)
    }
}
