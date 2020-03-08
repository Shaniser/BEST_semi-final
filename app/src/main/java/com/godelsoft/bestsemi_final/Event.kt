package com.godelsoft.bestsemi_final

class Event(var time: String,
            var sender: String,
            var header: String,
            var body: String
) {
    companion object {
        var events = mutableMapOf<Int, Event>()
        var counter = 0

        fun addEvent(event: Event) {
            events[counter++] = event
        }
    }
}