package com.example.quizkotlin.models

data class Quiz(
    var quizID: String,
    val questionList: ArrayList<Question>?
){
    fun questionList(trim: String) {

    }

    constructor() : this("", null)


}