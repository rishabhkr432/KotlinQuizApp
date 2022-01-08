package com.example.quizkotlin.fragments

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizkotlin.constants.FAILED

import com.example.quizkotlin.constants.SUCCESS
import com.example.quizkotlin.models.Question
import com.example.quizkotlin.models.Quiz
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import io.grpc.ClientStreamTracer

class QuestionViewModel : ViewModel() {
    val spinner: String = ""
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    private var _quizzes: MutableList<Quiz> = mutableListOf()
    val quizzesList: MutableList<Quiz>
        get() = _quizzes

    private val _options: MutableList<String?> = mutableListOf()
    val options: MutableList<String?>
        get() = _options


    private val _quizzesString = MutableLiveData<List<String>>()
    val quizzesListString: LiveData<List<String>>
        get() = _quizzesString

    private var _quiz = Quiz()


    fun getQuizzes(): String {
        var status = FAILED
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        database.collection("Quizzes").get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    Log.i(TAG, "No quizzes found")

                } else {
                    val quizList = ArrayList<Quiz>()
                    val quizListString = ArrayList<String>()
                    for (doc in it) {
                        val quiz = doc.toObject(Quiz::class.java)
                        quizList.add(quiz)
                        quizListString.add(quiz.quizId)
//                        if (quiz.questionsForQuiz.size > 0) {
//                            for (i in quiz.questionsForQuiz) {
//                                questionsList.add(i)
//                            }
//                        }
                        Log.d(TAG, quiz.toString())
                    }
                    _quizzes = quizList
                    _quizzesString.value = quizListString
//                    getSpinnerValue(view)
                    status = SUCCESS
                }
            }
        return status
    }





    internal var quiz: Quiz
        get() {return _quiz}
        set(value) {_quiz = value}


//internal var options:MutableLiveData<ArrayList<String>>
//    get() { return _options}
//    set(value) {_options = value}
//internal var quizzes:MutableLiveData<ArrayList<Quiz>>
//    get() { return _quizzes}
//    set(value) {_quizzes = value}
//
//    internal var quizTitle:MutableLiveData<ArrayList<String>>
//        get() { return _quizTitle}
//        set(value) {_quizTitle = value}

//internal var quizzes: Quiz
//    get() {return _quizzes}
//    set(value) {_quizzes = value}

// TODO: Implement the ViewModel
}