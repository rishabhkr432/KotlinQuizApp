package com.example.quizkotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizkotlin.models.User
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class StudentHomeActivity : AppCompatActivity() {
    private lateinit var collapsingToolbar: CollapsingToolbarLayout
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
//        setUser()

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        val user = auth.currentUser

        if (user != null) {
            database.collection("Users").document(user.uid).get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val doc = it.result
                        if (doc.exists()) {
                            val userDetail = doc.toObject(User::class.java)
                            if (userDetail != null) {
                                populateUserDetail(userDetail)
                            }
                        } else {
                            Toast.makeText(
                                this,
                                "Error while fetching your data from server: ${it.exception}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        }
//        user = auth.currentUser!!


        view_quiz_bank.setOnClickListener {

            val intent = Intent(this, QuizBank::class.java)
            startActivity(intent)
            Log.d(TAG, "Opening Quiz bank activity")

            finish()
        }
        check_marks.setOnClickListener {

            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
            Log.d(TAG, "Opening Check marks activity")

            finish()
        }
        tvLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            Log.d(TAG, "Exiting to login activity")
            finish()
        }

    }

    private fun initViews() {
        collapsingToolbar = findViewById(R.id.collapsing_toolbar)
        collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT)
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE)

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
    @SuppressLint("StaticFieldLeak")
    private fun populateUserDetail(user: User) {
        tvDisplayName.text = user.email[0].uppercaseChar().toString()
        tvEmail.text = user.email
        when (user.userType) {
            1 -> tvUserType.text = "Teacher"
            2 -> tvUserType.text = "Student"
            else -> tvUserType.text = "User Type Not determined"
        }

    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var studentHomeActivity: Activity
        private const val TAG = "StudentHomeActivity"
    }

}