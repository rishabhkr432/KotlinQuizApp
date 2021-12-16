package com.example.quizkotlin.models

data class Question(
    val questionID: String,
    val question: String,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String,
    val correct_answer: String
) {
    constructor() : this("", "", "", "", "","","")

}
