package com.example.quizkotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizkotlin.databinding.ActivityTeacherHomeBinding
import com.example.quizkotlin.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TeacherHomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTeacherHomeBinding
    private var subjectName = ""
    private lateinit var question_bank: TextView
    private lateinit var add_quiz: TextView
    private lateinit var add_question: TextView
    private lateinit var tvDisplayName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvUserType: TextView
    private lateinit var tvLogout: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_home)

        initViews()

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
        question_bank.setOnClickListener {

            val loginIntent = Intent(this, QuizBank::class.java)
            startActivity(loginIntent)
            finish()
        }
        add_question.setOnClickListener {

            val loginIntent = Intent(this, AddQuestion::class.java)
            startActivity(loginIntent)
            finish()
        }
        add_quiz.setOnClickListener {

            val loginIntent = Intent(this, AddQuiz::class.java)
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

        question_bank = findViewById(R.id.question_bank)
        add_quiz = findViewById(R.id.add_quiz)
        add_question = findViewById(R.id.add_question)
        tvLogout = findViewById(R.id.teacher_logout_tv)
        tvDisplayName = findViewById(R.id.toolbar_displayName_tv)
        tvEmail = findViewById(R.id.toolbar_email_tv)
        tvUserType = findViewById(R.id.toolbar_userType_tv)

    }
    @SuppressLint("SetTextI18n")
    private fun populateUserDetail(user: User) {
        tvDisplayName.text = user.email[0].uppercaseChar().toString()
        tvEmail.text = user.email
        when (user.userType) {
            1 -> tvUserType.text = "Teacher"
            2 -> tvUserType.text = "Student"
            else -> tvUserType.text = "User Type Not determined"
        }
    }
    init {
        teachershomeActivity = this
    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var teachershomeActivity: Activity
    }
}