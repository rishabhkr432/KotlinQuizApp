package com.example.quizkotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StudentHomeActivity : AppCompatActivity() {
    private lateinit var tvDisplayName: TextView
    private lateinit var view_quiz_bank: TextView
    private lateinit var check_marks: TextView
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


        view_quiz_bank.setOnClickListener {

            val intent = Intent(this, QuizBank::class.java)
            startActivity(intent)
            Log.d("${TAG}", "Opening Quiz bank activity")

            finish()
        }
        check_marks.setOnClickListener {

            val intent = Intent(this, CheckMarks::class.java)
            startActivity(intent)
            Log.d("${TAG}", "Opening Check marks activity")

            finish()
        }
        tvLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            Log.d("${TAG}", "Exiting to login activity")
            finish()
        }

    }

    private fun initViews() {

        check_marks = findViewById(R.id.marks)
        view_quiz_bank = findViewById(R.id.view_quiz)
        tvLogout = findViewById(R.id.student_logout_tv)
        tvDisplayName = findViewById(R.id.toolbar_displayName_tv)
        tvEmail = findViewById(R.id.toolbar_email_tv)
        tvUserType = findViewById(R.id.toolbar_userType_tv)

    }
    init {
        studentHomeActivity = this
    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var studentHomeActivity: Activity
        private const val TAG = "StudentHomeActivity"
    }

   }