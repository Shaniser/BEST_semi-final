package com.godelsoft.bestsemi_final


enum class Role {
    VISITOR, LBG;

    override fun toString(): String {
        return when(this) {
            LBG -> "LBG"
            VISITOR -> "VISITOR"
        }
    }
}