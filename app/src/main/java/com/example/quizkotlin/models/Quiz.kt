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
//    fun validation(quizTitle: String,docRef: DocumentReference): Boolean{
//        var result = false
//        var newQuiz = Quiz()
//        if (quizTitle == ""){
//            newQuiz = Quiz(quizTitle)
//            result = true
//        }
//        else{
//            if(quizTitle.matches(Constants.ALPHANUM.toRegex()) && quizTitle.length < Constants.LENGTHCHECK_50){
//                newQuiz = Quiz(quizTitle)
//                result = true
//            }
//            else{
//                result = false
//            }
//        }
//        return result
//    }
}
