package com.example.quizkotlin.activities
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizkotlin.*
import com.example.quizkotlin.adapters.AttemptQuizAdapter
import com.example.quizkotlin.adapters.SingleQuestionAdapter
import com.example.quizkotlin.Constants.ALPHANUM
import com.example.quizkotlin.Constants.INVALID_ONE_ANSWER
import com.example.quizkotlin.Constants.QUIZ_ID
import com.example.quizkotlin.Constants.STUDENTID
import com.example.quizkotlin.Constants.STUDENTSDISCLAIMER
import com.example.quizkotlin.Constants.STUDENT_QUIZ_PATH
import com.example.quizkotlin.Constants.STUDENT_QUIZ_RESULTS_PATH
import com.example.quizkotlin.Constants.TEACHERSDISCLAIMER
import com.example.quizkotlin.models.Question
import com.example.quizkotlin.models.Quiz
import com.example.quizkotlin.models.Results
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class AttemptQuizActivity : AppCompatActivity() {
    private var score: Int = 0
    private var questionsList: List<Question> = ArrayList()
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var next_button: MaterialButton
    private lateinit var answer: TextView
    private lateinit var question: TextView
    private lateinit var qnum_display: TextView
    private lateinit var options_rv: RecyclerView
    private var radioReturnAnswer: String = ""
    private var radioStoreAnswer: HashMap<String, String> = hashMapOf()
    private lateinit var options_progress: ProgressBar
    private lateinit var optionFailed: TextView
    private lateinit var attemptQuizAdapter: AttemptQuizAdapter
    private lateinit var singleQuizAdapter: SingleQuestionAdapter
    private lateinit var quizPas: Quiz
    private var userType :HashMap<Int, String> = hashMapOf<Int, String>()
    private var qNum: Int = 0
    private lateinit var hiddenDisclaimer: TextView
    private lateinit var goBackButton: ImageView
    private lateinit var currentQuestion: Question
    private var isValidation: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attempt_quiz)
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        user = auth.currentUser!!

        quizPas = intent.getSerializableExtra(QUIZ_PASS) as Quiz
        userType = intent.getSerializableExtra(USER_PASS) as HashMap<Int, String>
        Log.d("$TAG- userType", userType.toString())
        Log.d("$TAG-", "quizPas: $quizPas")
        initViews()
        /**
         * setting up quiz and questions .
         */
        questionsList = quizPas.quizQuestionList
        (questionsList as ArrayList<Question>).shuffle()
        currentQuestion = questionsList[qNum]
        setCurrentQuestion(currentQuestion)
        goBackButton.setOnClickListener {
            if (userType.containsKey(1)) {

                TeacherHomeActivity.teachershomeActivity.finish()
                val intent = Intent(this, TeacherHomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Quiz cannnot be closed once opened.", Toast.LENGTH_SHORT).show()

            }

        }
        next_button.setOnClickListener {
            Log.i("CheckingAnswer", radioReturnAnswer)
            Log.i("QuestionAnswer", radioStoreAnswer.toString())
            (validation(radioReturnAnswer))
            if(isValidation) {
                if (currentQuestion.correct_answer == radioReturnAnswer) {
                    score += 1
                    radioReturnAnswer = ""
                }
                if (qNum < questionsList.size) {
                    Toast.makeText(this, "Next Question", Toast.LENGTH_SHORT)
                        .show()
                    currentQuestion = questionsList[qNum]
                    setCurrentQuestion(currentQuestion)
                } else {

                    val quizResults =
                        Results(quizPas.quizId, score, user.email.toString(), radioStoreAnswer)
                    Log.d("resultsData", quizResults.toString())

                    if (userType.containsKey(2)) {
                        sendResultsToDatabase(quizResults)
                        Handler().postDelayed({
                            val intent = Intent(this, ResultsActivity::class.java)
                            intent.putExtra(ResultsActivity.SCORE_PASS, score)
                            startActivity(intent)
                            Log.d(TAG, "Opening Results activity")
                            finish()
                        }, 5000)
                    } else {
                        val intent = Intent(this, TeacherHomeActivity::class.java)
                        startActivity(intent)
                        Log.d(TAG, "Opening Teacher's activity")
                        finish()
                    }
                }
            }
            else{
//                Toast.makeText(this, "Try updating your input", Toast.LENGTH_SHORT).show()
                Log.i("Invalid_answer", "Try updating your input")
            }


        }
    }
    /**
     * Uploading results to the firebase .
     */
        private fun sendResultsToDatabase(quizResults: Results) {
            database.collection(STUDENT_QUIZ_RESULTS_PATH).document(quizPas.quizId).set(quizResults)
                .addOnSuccessListener {
                    Log.i("Results", "Sending results to database")
                        Toast.makeText(this, "Calculating Results", Toast.LENGTH_LONG).show()
                    database.collection(STUDENT_QUIZ_PATH)
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                if (document.get(QUIZ_ID) == (quizPas.quizId)) {
                                    document.reference.update(
                                        STUDENTID,
                                        FieldValue.arrayUnion(userType[2])
                                    )
                                    Toast.makeText(this, "Attempt recorded", Toast.LENGTH_SHORT).show()
                                    Log.d(
                                        "Updated doc",
                                        "updating doc - ${document.id} => ${document.data}"
                                    )
                                }

                            }
                        }


                }
                .addOnFailureListener {

                    Log.i("Results", "Failed to send results to database")

                }

        }

    /**
     * Setting all views .
     */
    private fun initViews() {
        qnum_display = findViewById(R.id.qnum_display)
        hiddenDisclaimer = findViewById(R.id.hiddenDisclaimer)
        question = findViewById(R.id.question_attempt)
        options_rv = findViewById(R.id.options_list_attempt_rv)
        next_button = findViewById(R.id.next_button_demo)
        optionFailed = findViewById(R.id.options_failed_attempt_tv)
        options_progress = findViewById(R.id.options_progress_attempt)
        goBackButton = findViewById(R.id.goBackButton_quiz_attempt)
        answer = findViewById(R.id.attempt_quiz_correct_answer_rv)
        options_rv.layoutManager = LinearLayoutManager(this)
        options_rv.setHasFixedSize(true)
        if (userType.containsKey(2)) {
            hiddenDisclaimer.text = STUDENTSDISCLAIMER
        }
        else{
            hiddenDisclaimer.text = TEACHERSDISCLAIMER
        }
    }
    /**
     * this method updates questions from the question list.
     */
    @SuppressLint("SetTextI18n")
    private fun setCurrentQuestion(currentQuestion: Question) {
        val optionsList = currentQuestion.options as ArrayList<String?>
        optionsList.shuffle()
        Log.i("optionsList", optionsList.toString())
        question.text = currentQuestion.question
        answer.text = "Correct Answer: ${currentQuestion.correct_answer}"
        if (userType.keys.contains(2)){
            answer.visibility = View.GONE
        }
        qNum += 1
        qnum_display.text = ("Total Questions: " + (qNum).toString() + "/${questionsList.size}")
        if (optionsList.isEmpty()) {
            options_progress.visibility = View.GONE
            optionFailed.visibility = View.VISIBLE
        } else if (optionsList.size > 1) {
            options_rv.visibility = View.VISIBLE
            optionFailed.visibility = View.GONE
            attemptQuizAdapter = AttemptQuizAdapter(
                optionsList,
                this,
                object : AttemptQuizAdapter.MyViewHolder.Listener {
                    override fun returnPosString(answer: String) {
                        radioReturnAnswer = answer
                        radioStoreAnswer[currentQuestion.question] = radioReturnAnswer
                        Log.d("radioReturnAnswer", "optionsList returned : $answer")
                    }
                })
            radioStoreAnswer[currentQuestion.question] = radioReturnAnswer
            options_rv.adapter = attemptQuizAdapter
        }else {
            if (userType.keys.contains(1)) {
                options_rv.visibility = View.GONE
            } else {
                optionFailed.visibility = View.GONE
                singleQuizAdapter = SingleQuestionAdapter(
                    optionsList,
                    this,
                    object : SingleQuestionAdapter.MyViewHolder.Listener {
                        override fun returnPosString(list: ArrayList<String?>) {
                            Log.d("returnList", list.toString())
                            radioReturnAnswer = list[0].toString()
                            Log.i("ReturnSingleAnswer", radioReturnAnswer)

                            radioStoreAnswer[currentQuestion.question] = radioReturnAnswer
                            Log.d(
                                "radioReturnAnswer",
                                "optionsList returned single answer : $radioReturnAnswer"
                            )
                        }

                    })

                radioStoreAnswer[currentQuestion.question] = radioReturnAnswer
                options_rv.adapter = singleQuizAdapter

            }
        }
    }
    /**
     * Errorchecking.
     */
    private fun validation(correctAnswer: String) {

        if (correctAnswer.matches(
            ALPHANUM.toRegex())){
            isValidation = true
            return
    }
        else{
            Toast.makeText(this, INVALID_ONE_ANSWER, Toast.LENGTH_SHORT).show()
            isValidation = false
            return
        }
}

    companion object {
        @SuppressLint("StaticFieldLeak")
        var QUIZ_PASS = "Quiz"
        var USER_PASS = "userType"
        private const val TAG = "AttemptQuizAdapter"
    }
}