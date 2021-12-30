package com.example.quizkotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizkotlin.models.Question
import com.example.quizkotlin.models.Quiz
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class AddQuestion : AppCompatActivity() {
    private lateinit var question: EditText
    private lateinit var answer: EditText
    private lateinit var spinnerSelectedText: String

    private var creatingOptionsList: ArrayList<String?> = arrayListOf()
    private lateinit var addQuestionadapter: AddQuestionAdapter
    private var quizList: ArrayList<Quiz> = arrayListOf()
    private var quizListString: ArrayList<String> = arrayListOf()
    private lateinit var goBackButton: ImageView
    private lateinit var save_button: MaterialButton
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var rv: RecyclerView
    private lateinit var options_progress: ProgressBar
    private lateinit var optionFailed: TextView
    private var minOptions: Int = 1
    private var isValidation: Boolean = false
//    private var validateOptions: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_question)
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
                        Log.d(TAG, quiz.toString())
                    }
                    getSpinnerValue()
                }
            }

        initViews()


        save_button.setOnClickListener {

            Log.d("correct_answer", answer.text.toString())
            Log.d("optionList", creatingOptionsList.toString())

            var questionTemp: String = question.text.toString().trim()
            var optionList: ArrayList<String?> = creatingOptionsList
            var correctAnswer = answer.text.toString().trim()


            validations(questionTemp, optionList, correctAnswer)
            if (isValidation) {
                saveClassToDatabase(questionTemp, optionList, correctAnswer)
            }



            goBackButton.setOnClickListener {
                TeacherHomeActivity.teachershomeActivity.finish()
                val intent = Intent(this, TeacherHomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun saveClassToDatabase(
        question_name: String,
        optionsList: ArrayList<String?>,
        correctAnswer: String
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
                    if (document.get("id") == (spinnerSelectedText)) {
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
                                    Toast.makeText(
                                        this,
                                        "Added, Total questions in the quiz: ${quizSize + 1}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    Log.d(
                                        TAG,
                                        "Question successfully added!"
                                    )

                                    finish()
                                    overridePendingTransition(0, 0)
                                    intent.putExtra(QUESTION, "")
                                    startActivity(intent)
                                    overridePendingTransition(0, 0)

                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                                    Log.w(
                                        TAG,
                                        "Error adding question",
                                        e
                                    )
//
                                    finish()
                                    startActivity(intent)
                                }
                        } else {
                            Toast.makeText(
                                this,
                                "Cannot add more questions to this quiz. Current size: $quizSize",
                                Toast.LENGTH_LONG
                            ).show()

                        }

                    }
                }
            }

    }

    private fun makeInputFieldEmpty() {
        question.setText("")
//        question.setText("")
        answer.setText("")

    }


    private fun initViews() {
        question = findViewById(R.id.add_question)
        answer = findViewById(R.id.correct_answer_rv)
        save_button = findViewById(R.id.save_button)
        goBackButton = findViewById(R.id.add_question_goBackButton)
        rv = findViewById(R.id.options_list_rv)
        optionFailed = findViewById(R.id.options_failed_tv)
        options_progress = findViewById(R.id.options_progress)
        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
        passedQuestionandSpinner()
        getOptions()

    }

    private fun getOptions() {
        for (i in 1..minOptions) {
            creatingOptionsList.add("")
        }
        Log.d("creatingOptionsList", creatingOptionsList.toString())
        if (creatingOptionsList.isEmpty()) {
            options_progress.visibility = View.GONE
            optionFailed.visibility = View.VISIBLE
        } else {
            optionFailed.visibility = View.GONE
            addQuestionadapter = AddQuestionAdapter(
                creatingOptionsList,
                this,
                object : AddQuestionAdapter.MyViewHolder.Listener {
                    override fun optionsReturn(list: ArrayList<String?>) {
                        creatingOptionsList = list
                        Log.d(TAG, "optionsList returned : $list")

                    }
                })
            rv.adapter = addQuestionadapter
        }

    }

    private fun getSpinnerValue() {
        val spinner = findViewById<Spinner>(R.id.spinner)
//            spinnerSelectedText = quizList.first().id


        if (spinner != null) {
            val adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, quizListString)
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
        Log.d("$TAG- getSpinnerValue() -", "quizList selected is $spinnerSelectedText")
//        return
    }

    private fun passedQuestionandSpinner() {
        val questionPassed = intent.getStringExtra(QUESTION) ?: return
        val spinnerPassed = intent.getStringExtra(SPINNER) ?: return
        val options = intent.getIntExtra(OPTIONS, 1)
        question.setText(questionPassed)
        spinnerSelectedText = spinnerPassed
        minOptions = options
        Log.d("$TAG- passedQuestionandSpinner - question", questionPassed)
        Log.d("$TAG- passedQuestionandSpinner - spinnerSelectedText", spinnerPassed)
        Log.d("$TAG- passedQuestionandSpinner - options", options.toString())
    }

    private fun validations(
        question_check: String,
        temp_optionList: ArrayList<String?>,
        correctAnswer: String
    ) {
        var validateOptions = 0
        if (question_check != "" && question_check.isNotEmpty()) {
            for (i in temp_optionList) {
                if (i != null) {
                    if (i != "" && i.isNotEmpty()) {
                        if (correctAnswer != "" && (correctAnswer.isNotEmpty() && temp_optionList.contains(
                                correctAnswer
                            ))
                        ) {
                            validateOptions += 1
                            if (validateOptions == minOptions) {
                                isValidation = true

                                return
                            }
                        } else {
                            answer.error = "Please enter correct answer"
                            answer.requestFocus()
                            isValidation = false
                            return
                        }
                    } else {
                        //                        creatingOptionsList.error = "Please enter option D"
                        //                        creatingOptionsList.requestFocus()
                        Toast.makeText(
                            this,
                            "Please enter valid option for the quiz",
                            Toast.LENGTH_SHORT
                        ).show()
                        isValidation = false
                        return
                    }
                }
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
        private const val TAG = "AddQuestion"
        var QUESTION = "Question"
        var SPINNER = "Spinner"
        var OPTIONS = "Options"
    }
}

