package com.example.quizkotlin.models

import java.io.Serializable

data class Question(
    val question: String,
    val options: ArrayList<String?>? = arrayListOf(),
    val correct_answer: String
) : Serializable {
    constructor() : this("", null, "")

}
