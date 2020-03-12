package com.godelsoft.bestsemi_final.model

import com.godelsoft.bestsemi_final.RawEvent


data class Event(
    val id: String,
    val event: RawEvent
) {
    constructor() : this("", RawEvent())

}
