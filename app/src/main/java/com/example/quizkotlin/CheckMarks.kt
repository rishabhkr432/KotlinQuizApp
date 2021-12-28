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
import com.example.quizkotlin.models.Quiz
import com.example.quizkotlin.models.Results
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class CheckMarks : AppCompatActivity() {

    private lateinit var goBackButton: ImageView
    private lateinit var checkMarksrv: RecyclerView
    private lateinit var emptyMessage: TextView
    private lateinit var progress: ProgressBar
    private lateinit var checkMarksAdapter: CheckMarksAdapter
    private var addquizcheck: Boolean = false
    private var newQuiz: Quiz? = null
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private var userType: Int = 0
    private var quizPath: String = "Quizzes"


    private var quizbanklist: ArrayList<Results> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkmarks_activity)


        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        initViews()
        fetchResults()
        goBackButton.setOnClickListener {

            StudentHomeActivity.studentHomeActivity.finish()
            val intent = Intent(this, StudentHomeActivity::class.java)
            startActivity(intent)
            finish()

        }


    }

    private fun initViews() {
        goBackButton = findViewById(R.id.quiz_marks_view_close_iv)
        checkMarksrv = findViewById(R.id.quiz_marks_view_rv)
        emptyMessage = findViewById(R.id.quiz_marks_view_tv)
        progress = findViewById(R.id.quiz_marks_view_progress)


        checkMarksrv.layoutManager = LinearLayoutManager(this)
        checkMarksrv.setHasFixedSize(true)
    }

    private fun fetchResults() {
        progress.visibility = View.VISIBLE

//
        database.collection("Student's quiz results").get()
            .addOnSuccessListener {
                progress.visibility = View.GONE
                if (it.isEmpty) {
                    emptyMessage.visibility = View.VISIBLE
                } else {
                    emptyMessage.visibility = View.GONE
                    for (doc in it) {
                        val quiz = doc.toObject(Results::class.java)
                        quizbanklist.add(quiz)
                    }
                    checkMarksAdapter = CheckMarksAdapter(
                        quizbanklist, this
                    )

                    checkMarksrv.adapter = checkMarksAdapter

                }
            }
            .addOnFailureListener {
                progress.visibility = View.GONE
                emptyMessage.visibility = View.VISIBLE
            }
    }
    companion object {
        private const val TAG = "CheckMarks"
    }
}