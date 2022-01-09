package com.example.quizkotlin.fragments

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizkotlin.Constants.FAILED

import com.example.quizkotlin.Constants.SUCCESS
import com.example.quizkotlin.Constants.TEACHERS_QUIZ_PATH
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

    /**
     * Pulling quizzes from the firebase
     */
    fun getQuizzes(): String {
        var status = FAILED
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        database.collection(TEACHERS_QUIZ_PATH).get()
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

                        Log.d(TAG, quiz.toString())
                    }
                    _quizzes = quizList
                    _quizzesString.value = quizListString
                    status = SUCCESS
                }
            }
        return status
    }





    internal var quiz: Quiz
        get() {return _quiz}
        set(value) {_quiz = value}

}