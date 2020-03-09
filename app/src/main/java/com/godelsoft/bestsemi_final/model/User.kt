package com.godelsoft.bestsemi_final.model


data class User(
    val name: String,
    val bio: String,
    val profilePicture: String?
) {
    constructor(): this("", "", null)

}
