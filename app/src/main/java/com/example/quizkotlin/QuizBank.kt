package com.example.quizkotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizkotlin.models.Question
import com.example.quizkotlin.models.Quiz
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

    private var quizbanklist: ArrayList<Quiz> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_quizzes_in_bank)

        initViews()

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        user = auth.currentUser!!
//        var intent = intent.extras
//        Log.d(TAG, "intent: " + intent)
//        if (intent != null) {
//            addquizcheck = intent!!.getBoolean("addquizcheck")
//            val docRef = database.collection("Quizzes").document()
//            database.collection("Quizzes").get()
////                .addSnapshotListener
////
////                            val quiz = doc.toObject(Quiz::class.java)
////                            if (assignment.done)
////            }
//
////            if (newQuiz != null) {
////                Log.d(TAG, "quizID: " + newQuiz!!.quizID)
////            }
//        }

//        Log.d(TAG, "addquizcheck: " + addquizcheck)

        fetchAvlSubject()


        ivClose.setOnClickListener {
            TeacherHomeActivity.teachershomeActivity.finish()
            val intent = Intent(this, TeacherHomeActivity::class.java)
            startActivity(intent)
            finish()
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
        database.collection("Quizzes").get()
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
                            quizbanklist,
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