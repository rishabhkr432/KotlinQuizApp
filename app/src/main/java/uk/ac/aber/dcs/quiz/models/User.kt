package com.example.quizkotlin.models

data class User(
    val uid: String,
    val email: String,
    val userType: Int,

    ) {
    constructor() : this("", "", -1)
}

