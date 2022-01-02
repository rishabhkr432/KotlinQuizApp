package com.example.quizkotlin.models

import java.io.Serializable

data class Quiz(
    var quizId: String,
    var studentId: ArrayList<String> = arrayListOf(),
    var questionsForQuiz: ArrayList<Question> = arrayListOf()


) : Serializable {
    constructor() : this("")
}
