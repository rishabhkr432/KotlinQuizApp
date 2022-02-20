package uk.ac.aber.dcs.quiz.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uk.ac.aber.dcs.quiz.R
import uk.ac.aber.dcs.quiz.adapters.QuizBankAdapter
import uk.ac.aber.dcs.quiz.constants.Constants.STUDENT_QUIZ_PATH
import uk.ac.aber.dcs.quiz.constants.Constants.TEACHERS_QUIZ_PATH
import uk.ac.aber.dcs.quiz.models.Quiz
import uk.ac.aber.dcs.quiz.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import uk.ac.aber.dcs.quiz.constants.Constants.USERS

class QuizBank : AppCompatActivity() {

    private lateinit var ivClose: ImageView
    private lateinit var rv: RecyclerView
    private lateinit var tvMsg: TextView
    private lateinit var progress: ProgressBar
    private lateinit var modifyQuestionSetting: QuizBankAdapter
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private var userEmail: String = ""
    private var quizPath: String = TEACHERS_QUIZ_PATH
    private var userType :HashMap<Int, String> = hashMapOf<Int, String>()
    private var quiz_bank_forwarding: ArrayList<Quiz> = arrayListOf()
    private var quiz_bank_incoming_firebase: ArrayList<Quiz> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_bank)

        initViews()

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        user = auth.currentUser!!


        database.collection(USERS).document(user.uid).
            /**
             * Reads the document referenced by this `DocumentReference`.
             *
             * @return A Task that will be resolved with the contents of the Document at this `DocumentReference`.
             */
        get(Source.DEFAULT)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val doc = it.result
                    if (doc.exists()) {
                        val userDetail = doc.toObject(User::class.java)
                        if (userDetail != null) {
                            userType[userDetail.userType] = userDetail.email
                            userEmail = userDetail.email

                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Error while fetching your data from server: ${it.exception}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                fetchAllQuizzes()
            }
        ivClose.setOnClickListener {
            if (userType.containsKey(1)){

                TeacherHomeActivity.teachershomeActivity.finish()
                val intent = Intent(this, TeacherHomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                StudentHomeActivity.studentHomeActivity.finish()
                val intent = Intent(this, StudentHomeActivity::class.java)
                startActivity(intent)
                finish()

            }

        }

    }
    /**
     * Setting views
     */
    private fun initViews() {
        ivClose = findViewById(R.id.go_back_button_quiz_bank)
        rv = findViewById(R.id.quiz_recycler_view)
        tvMsg = findViewById(R.id.failed_msg)
        progress = findViewById(R.id.progress_bar)
        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
    }
    /**
     * Downloading quizzes from the firebase.
     */
    private fun fetchAllQuizzes() {
        progress.visibility = View.VISIBLE
        if (userType.containsKey(2)) {
            quizPath = STUDENT_QUIZ_PATH
        }
//        Log.i(TAG, "userType:" + userType)
        Log.i("$TAG fetchAllQuizzes", "quizPath:$quizPath")
        database.collection(quizPath).get()
            .addOnSuccessListener {
                progress.visibility = View.GONE
                if (it.isEmpty) {
                    tvMsg.visibility = View.VISIBLE
                } else {
                    tvMsg.visibility = View.GONE
                    for (doc in it) {
                        val quiz = doc.toObject(Quiz::class.java)
                        quiz_bank_incoming_firebase.add(quiz)
                    }
                    checkExisting()
                    modifyQuestionSetting = QuizBankAdapter(
                        quiz_bank_forwarding, userType, quizPath,
                        this
                    )

                    rv.adapter = modifyQuestionSetting

                }
            }
            .addOnFailureListener {
                progress.visibility = View.GONE
                tvMsg.visibility = View.VISIBLE
            }
    }
    /**
     * This method removes the quiz if the student has already used their attempt.
     */
    private fun checkExisting() {
        for (i in quiz_bank_incoming_firebase){

            if (i.studentId?.contains(userEmail)!!.not() && userType.containsKey(2) || userType.containsKey(1)){
                quiz_bank_forwarding.add(i)
                Log.d(TAG, "Quiz added: ${i.studentId?.toString()} user: ${userType.values.toString()}")

            }
            else{
                Log.d(TAG, "Quiz removed: ${i.studentId?.toString()} user: ${userType.values.toString()}")
            }
        }
    }
    companion object {
        private const val TAG = "QuizBank"
    }

}