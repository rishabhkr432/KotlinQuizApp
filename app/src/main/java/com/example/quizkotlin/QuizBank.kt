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
import com.example.quizkotlin.models.Quiz
import com.example.quizkotlin.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class QuizBank : AppCompatActivity() {

    private lateinit var ivClose: ImageView
    private lateinit var rv: RecyclerView
    private lateinit var tvMsg: TextView
    private lateinit var progress: ProgressBar
    private lateinit var modifyQuestionSetting: ModifyQuestion
    private  var addquizcheck: Boolean = false
    private var newQuiz : Quiz? = null
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private  var userType: Int = 0
    private var quizPath: String  = "Quizzes"



    private var quizbanklist: ArrayList<Quiz> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_quizzes_in_bank)

        initViews()

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        user = auth.currentUser!!
//        val user = auth.currentUser

        database.collection("Users").document(user.uid).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val doc = it.result
                    if (doc.exists()) {
                        val userDetail = doc.toObject(User::class.java)
                        if (userDetail != null) {
                            userType = userDetail.userType
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Error while fetching your data from server: ${it.exception}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                fetchAvlSubject()
            }
        ivClose.setOnClickListener {
            if (userType == 1){

            TeacherHomeActivity.teachershomeActivity.finish()
            val intent = Intent(this, TeacherHomeActivity::class.java)
            startActivity(intent)
            finish()
        }
            else{
                StudentHomeActivity.studenthomeActivity.finish()
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
    private fun fetchAvlSubject() {
        progress.visibility = View.VISIBLE
        if (userType == 2){
            quizPath = "Student's quizzes"
        }
//        Log.i(TAG, "userType:" + userType)
        Log.i(TAG, "quizPath:$quizPath")
        database.collection(quizPath).get()
            .addOnSuccessListener {
                progress.visibility = View.GONE
                if (it.isEmpty) {
                    tvMsg.visibility = View.VISIBLE
                } else {
                    tvMsg.visibility = View.GONE
                    for (doc in it) {
                        val quiz = doc.toObject(Quiz::class.java)
                        quizbanklist.add(quiz)
                    }
                    modifyQuestionSetting = ModifyQuestion(
                            quizbanklist,userType,quizPath,
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

    companion object {
        private const val TAG = "QuizBank"
    }

}