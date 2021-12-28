package com.example.quizkotlin;
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
import com.example.quizkotlin.models.Results
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.ArrayList

class AttemptQuizActivity : AppCompatActivity() {
    private var score : Int = 0
    private var questionsList: List<Question> = ArrayList()
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var next_button: MaterialButton
    private lateinit var question: TextView
    private lateinit var qnum_display: TextView
  private var userType: Int = 1
    private var disclaimText: String = "Preview mode - Marks will not count"
    private lateinit var options_rv: RecyclerView
    private var radioReturnString: String = ""
    private var radioStoreAnswer: ArrayList<String> = arrayListOf()
    private lateinit var options_progress: ProgressBar
    private lateinit var optionFailed: TextView
private lateinit var attemptQuizAdapter: AttemptQuizAdapter
    private lateinit var quizPas: Quiz
    private var qNum : Int = 0
    private lateinit var hiddenDisclaimer: TextView
    private lateinit var goBack: ImageView
    private lateinit var currentQuestion : Question
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
    userType = intent.getSerializableExtra(USER_PASS) as Int
    Log.i("$TAG- userType", userType.toString())
    initViews()
    questionsList = quizPas.questionsForQuiz
    (questionsList as ArrayList<Question>).shuffle()
    currentQuestion = questionsList[qNum]
    setCurrentQuestion(currentQuestion)
//
    goBack.setOnClickListener {
        if (userType == 1){

            TeacherHomeActivity.teachershomeActivity.finish()
            val intent = Intent(this, TeacherHomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            StudentHomeActivity.studentHomeActivity.finish()
            val intent = Intent(this, StudentHomeActivity::class.java)
            startActivity(intent)
            finish()

        }

    }
    next_button.setOnClickListener {

        if (currentQuestion.correct_answer == radioReturnString) {
            score += 1
            radioReturnString = ""
            Toast.makeText(this, "Correct Answer!", Toast.LENGTH_SHORT)
                .show()

        } else {
            Toast.makeText(this, "Answer is wrong", Toast.LENGTH_SHORT)
                .show()
        }

//
//
        if (qNum < questionsList.size) {
            currentQuestion = questionsList[qNum]
            setCurrentQuestion(currentQuestion)
        } else {

            val quizResults = Results(quizPas.id, score, user.email.toString())

            if (userType == 2) {
                val ref = database.collection("Student's quiz results").document(quizPas.id)
                ref.set(quizResults)
                    .addOnSuccessListener {
                        Log.i("Results", "Sending results to database")
//                        Toast.makeText(this, "New quiz added", Toast.LENGTH_LONG).show()

//                    TURN THIS BACK ON


//                    TURN THIS BACK ON
//                    database.collection("Student's quizzes")
//                        .get()
//                        .addOnSuccessListener { documents ->
//                            for (document in documents) {
//                                if (document.get("id") == (quizPas.id)) {
//                                    document.reference.delete()
//                                    Toast.makeText(this, "Quiz deleted", Toast.LENGTH_SHORT).show()
//                                    Log.d(
//                                        "Document",
//                                        "Deleting - ${document.id} => ${document.data}"
//                                    )
//                                }
//
//                            }
//                        }
                    }
                    .addOnFailureListener {
//                        Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                        Log.i("Results", "Failed to send results to database")

                    }


                val intent = Intent(this, ResultsActivity::class.java)
                intent.putExtra(ResultsActivity.SCORE_PASS, score)
                startActivity(intent)
            }
            else{
                val intent = Intent(this, TeacherHomeActivity::class.java)
                startActivity(intent)
            }

            finish()
        }
    }
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
            goBack = findViewById(R.id.goBackButton_quiz_attempt)
            options_rv.layoutManager = LinearLayoutManager(this)
            options_rv.setHasFixedSize(true)
            if (userType == 1){
                hiddenDisclaimer.visibility = View.VISIBLE
                hiddenDisclaimer.text = disclaimText
            }
        }
//
private fun setCurrentQuestion(currentQuestion: Question) {
    var optionsList = currentQuestion.options as ArrayList<String?>
    optionsList.shuffle()
    Log.i("optionsList", optionsList.toString())
    question.text = currentQuestion.question
    qNum += 1

    qnum_display.text = ("Total Questions: " + (qNum).toString() + "/10")


    if (optionsList.isEmpty()) {
        options_progress.visibility = View.GONE
        optionFailed.visibility = View.VISIBLE
    } else {
        optionFailed.visibility = View.GONE
        attemptQuizAdapter = AttemptQuizAdapter(
            optionsList,
            this,
            object : AttemptQuizAdapter.MyViewHolder.Listener {
                override fun returnPosString(pos: String) {
                    radioReturnString = pos
                    radioStoreAnswer.add(radioReturnString)
                    Log.d("PosString", "optionsList returned : $pos")
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
//
}