package com.example.quizkotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizkotlin.models.Quiz
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class AddQuiz : AppCompatActivity() {
    private lateinit var title: EditText
    private lateinit var save_button: MaterialButton
    private lateinit var goBackButton: ImageView
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var newQuiz: Quiz
    private var addquiz: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_quiz)

        initViews()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        user = auth.currentUser!!
        Log.d(TAG, "Entered addQuiz activity")

        save_button.setOnClickListener {
            saveQuizToDatabase()

        }
        goBackButton.setOnClickListener {
            TeacherHomeActivity.teachershomeActivity.finish()
            val intent = Intent(this, TeacherHomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun saveQuizToDatabase() {
        val docRef = database.collection("Quizzes").document()
        if (title.text.toString().trim() == "") {
            newQuiz = Quiz(docRef.id)
        } else {
            newQuiz = Quiz(title.text.toString().trim())
        }
        docRef.set(newQuiz)
            .addOnSuccessListener {
                Toast.makeText(this, "New quiz added", Toast.LENGTH_LONG).show()
                makeInputFieldEmpty()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                makeInputFieldEmpty()
            }
    }

    private fun makeInputFieldEmpty() {
        title.setText("")

    }

    private fun initViews() {
        title = findViewById(R.id.add_title)

        save_button = findViewById(R.id.save_button_quiz)
        goBackButton = findViewById(R.id.goBackButton_quiz)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private const val TAG = "AddQuiz"
    }
}