package com.example.quizkotlin.fragments

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizkotlin.constants.FAILED
import com.example.quizkotlin.constants.NULLCHECK
import com.example.quizkotlin.constants.SAVING
import com.example.quizkotlin.constants.OVERLOADED
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

    private val _quizzes = MutableLiveData<List<Quiz>>()
    val quizzesList: LiveData<List<Quiz>>
    get() = _quizzes

    private val _options: MutableList<String?> = mutableListOf()
    var options: MutableList<String?> = mutableListOf()
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
                    _quizzes.value = quizList
                    _quizzesString.value = quizListString
//                    getSpinnerValue(view)
                    status = SUCCESS
                }
            }
        return status
    }
         fun saveClassToDatabase(
            question_name: String,
            optionsList: MutableList<String?>,
            correctAnswer: String,
            quiz: Quiz

        ) {

            val newQuestion = Question(

                question_name,
                optionsList,
                correctAnswer
            )
            database.collection("Quizzes")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.get("quizId") == (quiz.quizId)) {
////
                            val quiz = (document.toObject(Quiz::class.java))
                            val quizSize = quiz.questionsForQuiz.size
                            Log.d(TAG, quiz.toString())
//
//
                            if (quizSize < 10) {
//
                                document.reference.update(
                                    "questionsForQuiz",
                                    FieldValue.arrayUnion(newQuestion)

                                )
                                    .addOnSuccessListener {
                                        Log.d(
                                            TAG,
                                            "Question successfully saved. Uploading.....!"
                                        )


//                                        (requireActivity() as AddQuestion).restartFragment(R.id.askQuestion)
//                                        makeInputFieldEmpty()




                                    }
                                    .addOnFailureListener { e ->
//                                        Toast.makeText(view.context, "Failed", Toast.LENGTH_LONG).show()
                                        Log.w(
                                            TAG,
                                            "Error adding question",
                                            e
                                        )

//                                        (requireActivity() as AddQuestion).restartFragment(R.id.askQuestion)
                                    }
                            } else {
                                Log.i(TAG,"Cannot add more questions to this quiz. Current size: $quizSize")


                            }

                        }
//                        else{
//                            Log.i(TAG,"Quiz not found ${quiz.quizId}")
//                            status = "${quiz.quizId} not found"
//
//                        }
                    }
                }


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