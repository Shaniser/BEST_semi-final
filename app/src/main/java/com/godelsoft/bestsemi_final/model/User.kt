package com.godelsoft.bestsemi_final.model


data class User(
    val name: String,
    val bio: String,
    val profilePicture: String?,
    val registrationTokens: MutableList<String>
) {
    constructor(): this("", "", null, mutableListOf())

}
