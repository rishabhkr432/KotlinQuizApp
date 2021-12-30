package com.example.quizkotlin.models

import java.io.Serializable

data class Results(
    var quizID: String,
    var results: Int,
    var studentId: String,
    var storeStudentAnswers: HashMap<String, String> = hashMapOf<String, String>()
) : Serializable {
    constructor() : this("", 0, "", HashMap<String, String>())
}
