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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class QuestionBank : AppCompatActivity() {

    private lateinit var ivClose: ImageView
    private lateinit var rv: RecyclerView
    private lateinit var tvMsg: TextView
    private lateinit var progress: ProgressBar

    private lateinit var showquestions: ShowQuestions

    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    private var questionbanklist: ArrayList<Question> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_questions_in_bank)

        initViews()

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        user = auth.currentUser!!

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
        database.collection("Questions").get()
            .addOnSuccessListener {
                progress.visibility = View.GONE
                if (it.isEmpty) {
                    tvMsg.visibility = View.VISIBLE
                } else {
                    tvMsg.visibility = View.GONE
                    for (doc in it) {
                        val subject = doc.toObject(Question::class.java)
                        questionbanklist.add(subject)
                    }
                    showquestions = ShowQuestions(
                        questionbanklist,
                        this
                    )
                    rv.adapter = showquestions
                }
            }
            .addOnFailureListener {
                progress.visibility = View.GONE
                tvMsg.visibility = View.VISIBLE
            }
    }

    companion object {
        private const val TAG = "QuestionBank"
    }

}