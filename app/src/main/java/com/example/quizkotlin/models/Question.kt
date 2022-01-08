package com.example.quizkotlin.models

import androidx.lifecycle.MutableLiveData
import java.io.Serializable

data class Question(
    val question: String,
    val options: MutableList<String?>? = arrayListOf(),
    val correct_answer: String
) : Serializable {
    constructor() : this("", null, "")

}
