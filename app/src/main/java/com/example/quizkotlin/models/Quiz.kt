package com.example.quizkotlin.models
import java.io.Serializable
data class Quiz (
    var id: String,
    var results: Int = 0,
    var questionsForQuiz: ArrayList<Question> = arrayListOf()

): Serializable
{constructor() : this("") }
