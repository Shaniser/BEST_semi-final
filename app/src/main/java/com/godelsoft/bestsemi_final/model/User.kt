package com.godelsoft.bestsemi_final.model

import com.godelsoft.bestsemi_final.Role


data class User(
    val name: String,
    val bio: String,
    val role: Role,
    val profilePicture: String?,
    val registrationTokens: MutableList<String>
) {
    constructor(): this("", "", Role.VISITOR, null, mutableListOf())

}
