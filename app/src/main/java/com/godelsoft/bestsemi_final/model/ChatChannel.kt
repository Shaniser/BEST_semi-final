package com.godelsoft.bestsemi_final.model


data class ChatChannel(val userIds: MutableList<String>) {
    constructor() : this(mutableListOf())
}
