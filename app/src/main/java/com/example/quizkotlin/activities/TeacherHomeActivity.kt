package com.example.quizkotlin.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizkotlin.R
import com.example.quizkotlin.models.User
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

class TeacherHomeActivity : AppCompatActivity() {
    private lateinit var collapsingToolbar: CollapsingToolbarLayout
    private lateinit var quiz_bank: TextView
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
        quiz_bank.setOnClickListener {

            val intent = Intent(this, QuizBank::class.java)
            startActivity(intent)
            Log.d("$TAG", "Opening Quiz Bank activity")
            finish()
        }
        add_question.setOnClickListener {

            val intent = Intent(this, AddQuestion::class.java)
            startActivity(intent)
            Log.d("$TAG", "Opening Add question activity")
            finish()
        }
        add_quiz.setOnClickListener {

            val intent = Intent(this, AddQuiz::class.java)
            startActivity(intent)
            Log.d("$TAG", "Opening Add quiz activity")
            finish()
        }
        tvLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            Log.d("$TAG", "Exiting to login activity")
            finish()
        }
    }

    private fun initViews() {
        collapsingToolbar = findViewById(R.id.collapsing_toolbar)
        collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT)
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE)
        quiz_bank = findViewById(R.id.quiz_bank)
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

        private const val TAG = "TeacherHomeActivity"

    }
}