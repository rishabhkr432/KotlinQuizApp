package com.example.quizkotlin

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
import com.example.quizkotlin.constants.STUDENT_QUIZ_PATH
import com.example.quizkotlin.constants.TEACHERS_QUIZ_PATH
import com.example.quizkotlin.models.Quiz
import com.example.quizkotlin.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

class QuizBank : AppCompatActivity() {

    private lateinit var ivClose: ImageView
    private lateinit var rv: RecyclerView
    private lateinit var tvMsg: TextView
    private lateinit var progress: ProgressBar
    private lateinit var modifyQuestionSetting: ModifyQuestion
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
//    private var userType: Int = 0
    private var userEmail: String = ""
    private var quizPath: String = TEACHERS_QUIZ_PATH
    private var userType :HashMap<Int, String> = hashMapOf<Int, String>()
    private var quiz_bank_forwarding: ArrayList<Quiz> = arrayListOf()
    private var quiz_bank_incoming_firebase: ArrayList<Quiz> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_quizzes_in_bank)

        initViews()

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        user = auth.currentUser!!
//        val user = auth.currentUser

        database.collection("Users").document(user.uid).
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


    private fun initViews() {
        ivClose = findViewById(R.id.avlSubject_close_iv)
        rv = findViewById(R.id.avlSubject_rv)
        tvMsg = findViewById(R.id.avlSubject_msg_tv)
        progress = findViewById(R.id.avlSubject_progress)
        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
    }

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
                    modifyQuestionSetting = ModifyQuestion(
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
    private fun checkExisting() {
        for (i in quiz_bank_incoming_firebase){

            if (i.studentId.contains(userEmail).not() && userType.containsKey(2) || userType.containsKey(1)){
                quiz_bank_forwarding.add(i)
                Log.d(TAG, "Quiz added: ${i.studentId.toString()} user: ${userType.values.toString()}")

            }
            else{
                Log.d(TAG, "Quiz removed: ${i.studentId.toString()} user: ${userType.values.toString()}")
            }
        }
    }
    companion object {
        private const val TAG = "QuizBank"
    }

}