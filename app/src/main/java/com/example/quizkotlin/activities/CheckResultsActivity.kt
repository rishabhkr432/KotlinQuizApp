package com.example.quizkotlin.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizkotlin.adapters.CheckResultsAdapter
import com.example.quizkotlin.R
import com.example.quizkotlin.Constants.QUIZ_ID
import com.example.quizkotlin.Constants.STUDENT_QUIZ_PATH
import com.example.quizkotlin.models.Question
import com.example.quizkotlin.models.Quiz
import com.example.quizkotlin.models.Results
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CheckResultsActivity : AppCompatActivity() {

    private lateinit var goBackButton: ImageView
    private lateinit var checkResultsRv: RecyclerView
    private lateinit var emptyMessage: TextView
    private lateinit var progress: ProgressBar
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var qNum: Int = 0
    private lateinit var next_button: MaterialButton
    private lateinit var checkResultsAdapter: CheckResultsAdapter
    private var userNotFound: String = "User input not found"
    private lateinit var disclaimer: TextView
    private lateinit var resultPassed: Results
    private var setQuiz: Quiz? = null
    private var questionsList: List<Question> = ArrayList()
    private lateinit var currentQuestion: Question
    private lateinit var question: TextView
    private lateinit var correctAnswer: TextView
    private lateinit var qnum_display: TextView
    private lateinit var quizTitle: TextView
    private var showAnswer: Boolean = false
    private lateinit var userAnswer: String
    private var quizQuestionUserAnswer: HashMap<String, String> = hashMapOf<String, String>()
    private var quiz: String = "Quiz"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_results)
        resultPassed = intent.getSerializableExtra(RESULT_PASS) as Results
        Log.i(TAG, "Result_received: ${resultPassed.quizId}")
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        initViews()
        progress.visibility = View.VISIBLE
        database.collection(STUDENT_QUIZ_PATH)
            .get()
            .addOnSuccessListener { documents ->
                progress.visibility = View.GONE
                for (document in documents) {
                    if (document.get(QUIZ_ID) == (resultPassed.quizId)) {
                        emptyMessage.visibility = View.GONE
////
                        setQuiz = (document.toObject(Quiz::class.java))
                        questionsList = setQuiz!!.quizQuestionList
                        quizQuestionUserAnswer = resultPassed.storeStudentAnswers
                        quizTitle.text = setQuiz?.quizId
                        currentQuestion = questionsList[qNum]
                        setCurrentQuestion(currentQuestion)
                    }
                    else{
                        Log.i(TAG, "${resultPassed.quizId} not found")
                        quizTitle.text = quiz
                        currentQuestion = Question("", null, "")
                    }
                }


            }.addOnFailureListener {
                progress.visibility = View.GONE
                emptyMessage.visibility = View.VISIBLE
            }
        next_button.setOnClickListener {


            if (qNum < questionsList.size) {
                currentQuestion = questionsList[qNum]
                setCurrentQuestion(currentQuestion)
            } else {
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
        goBackButton.setOnClickListener {
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
                finish()
            }
    }
    /**
     * setting views
     */

    private fun initViews() {
        qnum_display = findViewById(R.id.results_qnum_display)
        goBackButton = findViewById(R.id.results_goBackButton)
        checkResultsRv = findViewById(R.id.results_options_list_rv)
        emptyMessage = findViewById(R.id.results_options_failed_tv)
        progress = findViewById(R.id.results_options_progress)
        question = findViewById(R.id.results_show_question)
        correctAnswer = findViewById(R.id.results_correct_answer_rv)
        next_button = findViewById(R.id.results_next_button)
        quizTitle = findViewById(R.id.results_quiz_title)
        disclaimer = findViewById(R.id.hidden_disclaimer_check_activity)




        checkResultsRv.layoutManager = LinearLayoutManager(this)
        checkResultsRv.setHasFixedSize(true)

    }
    /**
     * this method updates questions from the question list.
     */

    @SuppressLint("SetTextI18n")
    private fun setCurrentQuestion(currentQuestion: Question) {

        val optionsList = currentQuestion.options as ArrayList<String?>


        Log.i("optionsList", optionsList.toString())
        question.text = currentQuestion.question
        qNum += 1

        qnum_display.text = ("Total Questions: " + (qNum).toString() + "/${questionsList.size}")
        correctAnswer.text = "Correct Answer: ${currentQuestion.correct_answer}"

        when {
            optionsList.size == 1 -> {
                optionsList.clear()
                disclaimer.visibility = View.GONE
                userAnswer = quizQuestionUserAnswer[currentQuestion.question].toString()
                if (userAnswer == ""){userAnswer = userNotFound
//                    disclaimer.visibility = View.VISIBLE
                }
                correctAnswer.visibility = View.VISIBLE
                Log.i("$TAG-", "userAnswer: $userAnswer")
                optionsList.add(userAnswer)

            }
            quizQuestionUserAnswer.containsKey(currentQuestion.question) -> {
                userAnswer = quizQuestionUserAnswer[currentQuestion.question].toString()
                if (userAnswer == ""){disclaimer.visibility = View.VISIBLE}
                correctAnswer.visibility = View.GONE
                showAnswer = true
                Log.i("$TAG-", "userAnswer: $userAnswer")

            }
            else -> {
                userAnswer = userNotFound
                optionsList.clear()
                optionsList.add(userAnswer)

            }
        }
            checkResultsAdapter = CheckResultsAdapter(
                optionsList, userAnswer, currentQuestion.correct_answer, showAnswer, this
            )

            checkResultsRv.adapter = checkResultsAdapter




    }


    companion object {
        @SuppressLint("StaticFieldLeak")
        var RESULT_PASS = "Result"
        private const val TAG = "CheckResultsActivity"

    }
}