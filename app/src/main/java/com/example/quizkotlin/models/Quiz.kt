package com.example.quizkotlin.models

import com.example.quizkotlin.Constants
import com.google.firebase.firestore.DocumentReference
import java.io.Serializable

data class Quiz(
    var quizId: String,
    var studentId: MutableList<String?>? = mutableListOf(),
    var quizQuestionList: ArrayList<Question> = arrayListOf()
) : Serializable {
    constructor() : this("")
    override fun toString(): String {
        return quizId
    }

}
