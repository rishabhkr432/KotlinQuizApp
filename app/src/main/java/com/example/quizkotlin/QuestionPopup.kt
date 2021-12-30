package com.example.quizkotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.quizkotlin.models.Quiz
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class QuestionPopup : AppCompatActivity() {
    private lateinit var question: EditText
    private lateinit var optionsNumber: EditText
    private var quizList: ArrayList<Quiz> = arrayListOf()
    private var quizListString: ArrayList<String> = arrayListOf()
    private lateinit var goBackButton: ImageView

    private lateinit var next_button: MaterialButton
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var spinnerSelectedText: String

    private lateinit var rv: RecyclerView
    private lateinit var ivClose: ImageView
    private lateinit var options_progress: ProgressBar
    private lateinit var optionFailed: TextView
    private var isValidation: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.question_popup)
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        user = auth.currentUser!!
        database.collection("Quizzes").get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    Toast.makeText(this, "No quizzes found", Toast.LENGTH_SHORT).show()
                } else {
                    for (doc in it) {
                        val quiz = doc.toObject(Quiz::class.java)
                        quizList.add(quiz)
                        quizListString.add(quiz.id)
//                        if (quiz.questionsForQuiz.size > 0) {
//                            for (i in quiz.questionsForQuiz) {
//                                questionsList.add(i)
//                            }
//                        }
                        Log.d(TAG, quiz.toString())
                    }
                    getSpinnerValue()
                }
            }
        initViews()

        next_button.setOnClickListener {
            var questionTemp: String = question.text.toString().trim()
            var optionsTemp: Int = optionsNumber.text.toString().trim().toIntOrNull() ?: 4
            Log.d(TAG, "options: $optionsTemp")
            validations(
                questionTemp,
                optionsTemp
            )
            if (isValidation) {
//                getOptions(options)

                val intent = Intent(this, AddQuestion::class.java)
                intent.putExtra(AddQuestion.QUESTION, questionTemp)
                intent.putExtra(AddQuestion.SPINNER, spinnerSelectedText)
                intent.putExtra(AddQuestion.OPTIONS, optionsTemp)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun initViews() {
        question = findViewById(R.id.popup_add_question)
        optionsNumber = findViewById(R.id.popup_options)
        next_button = findViewById(R.id.popup_next_button)
        goBackButton = findViewById(R.id.popup_goBackButton)

//        rv = findViewById(R.id.options_list_rv)
//        optionFailed = findViewById(R.id.options_failed_tv)
//        options_progress = findViewById(R.id.options_progress)
//        rv.layoutManager = LinearLayoutManager(this)
//        rv.setHasFixedSize(true)
//        Resetting default value
//        optionsNumber.setText("4")
//        Log.i(TAG, optionsNumber.text.toString().trim())

        print("Info taken")


    }

    //    private fun getOptions(MAXOPTIONS: Int){
//        for (i in 1..MAXOPTIONS){
//            creatingOptionsList.add(i)
//        }
//        Log.i(TAG, creatingOptionsList.toString())
////        options_progress.visibility = View.VISIBLE
//        optionFailed.visibility = View.VISIBLE
//        addQuestionadapter = AddQuestionAdapter(creatingOptionsList,this)
//        rv.adapter = addQuestionadapter
//
//    }
    private fun getSpinnerValue() {
        val spinner = findViewById<Spinner>(R.id.popup_spinner)
        spinnerSelectedText = quizList.first().id


        if (spinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, quizListString)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    spinnerSelectedText = quizList[position].id
                    println("QuizList selected is $spinnerSelectedText")  // <-- this works
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

// selectedText is not seen here:
        println("quizList selected is $spinnerSelectedText")
//        return
    }

    private fun validations(
        question_check: String,
        options: Int,

        ) {
        if (question_check != "" && !question_check.isEmpty()) {
            if (options in 1..10) {
                isValidation = true
                return
            } else {

                optionsNumber.error = "Please enter correct answer"
                optionsNumber.requestFocus()
                isValidation = false
                return
            }
        } else {
            question.error = "Please enter question"
            question.requestFocus()
            isValidation = false
            return

        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private const val TAG = "QuestionPopup"
        lateinit var QuestionPopup: Activity
    }

}


