package com.example.quizkotlin
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
import com.example.quizkotlin.constants.STUDENT_QUIZ_RESULTS_PATH
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
    private var disclaimText: String = "Preview mode - Marks will not count"
    private lateinit var options_rv: RecyclerView
    private var radioReturnAnswer: String = ""
    private var radioStoreAnswer: HashMap<String, String> = hashMapOf<String, String>()
    private lateinit var options_progress: ProgressBar
    private lateinit var optionFailed: TextView
    private lateinit var attemptQuizAdapter: AttemptQuizAdapter
    private lateinit var quizPas: Quiz
    private var userType :HashMap<Int, String> = hashMapOf<Int, String>()
    private var qNum: Int = 0
    private lateinit var hiddenDisclaimer: TextView
    private lateinit var goBackButton: ImageView
    private lateinit var currentQuestion: Question
    var radioGroup: RadioGroup? = null

    //
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attempt_quiz)
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        user = auth.currentUser!!
//
//
        quizPas = intent.getSerializableExtra(QUIZ_PASS) as Quiz
        userType = intent.getSerializableExtra(USER_PASS) as HashMap<Int, String>
        Log.d("$TAG- userType", userType.toString())
        Log.d("$TAG", "quizPas: $quizPas")
        initViews()
        questionsList = quizPas.questionsForQuiz
        (questionsList as ArrayList<Question>).shuffle()
        currentQuestion = questionsList[qNum]
        setCurrentQuestion(currentQuestion)
//
        goBackButton.setOnClickListener {
            if (userType.containsKey(1)) {

                TeacherHomeActivity.teachershomeActivity.finish()
                val intent = Intent(this, TeacherHomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Quiz cannnot be closed once opened.", Toast.LENGTH_SHORT).show()
//                StudentHomeActivity.studentHomeActivity.finish()
//                val intent = Intent(this, StudentHomeActivity::class.java)
//                startActivity(intent)
//                finish()

            }

        }
        next_button.setOnClickListener {

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
                    Results(quizPas.id, score, user.email.toString(), radioStoreAnswer)
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
                }
                else{
                    val intent = Intent(this, TeacherHomeActivity::class.java)
                            startActivity(intent)
                            Log.d(TAG, "Opening Teacher's activity")
                            finish()
                }
                }


        }
    }
        private fun sendResultsToDatabase(quizResults: Results) {
            database.collection(STUDENT_QUIZ_RESULTS_PATH).document(quizPas.id).set(quizResults)
//                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
//                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
//        }
                .addOnSuccessListener {
                    Log.i("Results", "Sending results to database")
                        Toast.makeText(this, "Calculating Results", Toast.LENGTH_LONG).show()

//                    TURN THIS BACK ON


//                    TURN THIS BACK ON
                    database.collection("Student's quizzes")
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                if (document.get("id") == (quizPas.id)) {
                                    document.reference.update(
                                        "studentId",
                                        FieldValue.arrayUnion(userType[2])
                                    )
                                    Toast.makeText(this, "Attempt recorded", Toast.LENGTH_SHORT).show()
                                    Log.d(
                                        "Updated doc",
                                        "Deleting - ${document.id} => ${document.data}"
                                    )
                                }

                            }
                        }


                }
                .addOnFailureListener {
//                        Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                    Log.i("Results", "Failed to send results to database")
//                            val intent = Intent(this, TeacherHomeActivity::class.java)
//                            startActivity(intent)
//                            Log.d(TAG, "Opening Teacher's activity")
//                            finish()
////                    finish()
                }
//
//                        }
//                else{
//                    Log.d("Breaks","Something is wrong")
        }


    private fun initViews() {
        qnum_display = findViewById(R.id.qnum_display)
        hiddenDisclaimer = findViewById(R.id.hiddenDisclaimer)
        question = findViewById(R.id.question_attempt)
        options_rv = findViewById(R.id.options_list_attempt_rv)
        radioGroup = findViewById(R.id.radioGrp)
        next_button = findViewById(R.id.next_button_demo)
        optionFailed = findViewById(R.id.options_failed_attempt_tv)
        options_progress = findViewById(R.id.options_progress_attempt)
        goBackButton = findViewById(R.id.goBackButton_quiz_attempt)
        answer = findViewById(R.id.attempt_quiz_correct_answer_rv)
        options_rv.layoutManager = LinearLayoutManager(this)
        options_rv.setHasFixedSize(true)
        if (userType.containsKey(1)) {
            hiddenDisclaimer.visibility = View.VISIBLE
            hiddenDisclaimer.text = disclaimText
        }
    }

    private fun setCurrentQuestion(currentQuestion: Question) {
        var optionsList = currentQuestion.options as ArrayList<String?>
        optionsList.shuffle()
        Log.i("optionsList", optionsList.toString())
        question.text = currentQuestion.question
        answer.text = "Correct Answer - ${currentQuestion.correct_answer}"
        if (userType.keys.contains(2)){
            answer.visibility = View.GONE
        }
        qNum += 1
        qnum_display.text = ("Total Questions: " + (qNum).toString() + "/${questionsList.size}")
        if (optionsList.isEmpty()) {
            options_progress.visibility = View.GONE
            optionFailed.visibility = View.VISIBLE
        } else {
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
            options_rv.adapter = attemptQuizAdapter
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var QUIZ_PASS = "Quiz"
        var USER_PASS = "userType"
        private const val TAG = "AttemptQuizAdapter"
    }
}