package com.example.quizkotlin.models
import java.io.Serializable
data class Quiz (
    var id: String,
    var questionsForQuiz: ArrayList<Question> = arrayListOf()
): Serializable
{constructor() : this("") }
