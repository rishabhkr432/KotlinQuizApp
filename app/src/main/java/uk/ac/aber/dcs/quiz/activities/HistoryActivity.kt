package com.example.quizkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizkotlin.adapters.HistoryAdapter
import com.example.quizkotlin.R
import com.example.quizkotlin.Constants.STUDENT_QUIZ_RESULTS_PATH
import com.example.quizkotlin.models.Results
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HistoryActivity : AppCompatActivity() {

    private lateinit var goBackButton: ImageView
    private lateinit var historyRv: RecyclerView
    private lateinit var emptyMessage: TextView
    private lateinit var progress: ProgressBar
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private var quizbanklist: ArrayList<Results> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)


        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        initViews()
        fetchQuizHistory()
        goBackButton.setOnClickListener {

            StudentHomeActivity.studentHomeActivity.finish()
            val intent = Intent(this, StudentHomeActivity::class.java)
            startActivity(intent)
            finish()

        }


    }
    /**
     * Setting views
     */
    private fun initViews() {
        goBackButton = findViewById(R.id.quiz_marks_view_close_iv)
        historyRv = findViewById(R.id.quiz_marks_view_rv)
        emptyMessage = findViewById(R.id.quiz_marks_view_tv)
        progress = findViewById(R.id.quiz_marks_view_progress)


        historyRv.layoutManager = LinearLayoutManager(this)
        historyRv.setHasFixedSize(true)
    }
    /**
     * Downloading quizzes from the firebase.
     */
    private fun fetchQuizHistory() {
        progress.visibility = View.VISIBLE

//
        database.collection(STUDENT_QUIZ_RESULTS_PATH).get()
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
                    historyAdapter = HistoryAdapter(
                        quizbanklist, this
                    )

                    historyRv.adapter = historyAdapter

                }
            }
            .addOnFailureListener {
                progress.visibility = View.GONE
                emptyMessage.visibility = View.VISIBLE
            }
    }

    companion object {
        private const val TAG = "HistoryActivity"
    }
}