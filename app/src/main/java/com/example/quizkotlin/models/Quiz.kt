package com.example.quizkotlin.models

data class Quiz(
    var id: String,
    var questionsForQuiz: ArrayList<Question> = arrayListOf()
){constructor() : this("") }
