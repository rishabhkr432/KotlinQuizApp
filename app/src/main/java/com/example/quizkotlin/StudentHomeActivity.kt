package com.example.quizkotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StudentHomeActivity : AppCompatActivity() {
    private lateinit var tvDisplayName: TextView
    private lateinit var viewQuizBank: TextView
    private lateinit var checkMarks: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvUserType: TextView
    private lateinit var tvLogout: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_home)

        initViews()

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()


        viewQuizBank.setOnClickListener {

            val loginIntent = Intent(this, QuizBank::class.java)
            startActivity(loginIntent)
            finish()
        }
        checkMarks.setOnClickListener {

            val loginIntent = Intent(this, CheckMarks::class.java)
            startActivity(loginIntent)
            finish()
        }
        tvLogout.setOnClickListener {
            auth.signOut()
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }

    }

    private fun initViews() {

        checkMarks = findViewById(R.id.marks)
        viewQuizBank = findViewById(R.id.view_quiz)
        tvLogout = findViewById(R.id.student_logout_tv)
        tvDisplayName = findViewById(R.id.toolbar_displayName_tv)
        tvEmail = findViewById(R.id.toolbar_email_tv)
        tvUserType = findViewById(R.id.toolbar_userType_tv)

    }
    init {
        studenthomeActivity = this
    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var studenthomeActivity: Activity
    }

   }