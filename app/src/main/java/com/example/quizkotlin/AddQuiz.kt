package com.example.quizkotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizkotlin.constants.ALPHANUM
import com.example.quizkotlin.constants.LENGTHCHECK_50
import com.example.quizkotlin.constants.TEACHERS_QUIZ_PATH
import com.example.quizkotlin.models.Quiz
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

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
            getQuizInput()

        }
        goBackButton.setOnClickListener {
            TeacherHomeActivity.teachershomeActivity.finish()
            val intent = Intent(this, TeacherHomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getQuizInput() {
        val tempTitle = title.text.toString().trim()
        val docRef = database.collection(TEACHERS_QUIZ_PATH).document()
        if (title.text.toString().trim() == "") {
            newQuiz = Quiz(docRef.id)
            sendToDatabase(docRef)
        } else {
//            matches(ALPHANUM.toRegex()
            if(validation(tempTitle)){
                newQuiz = Quiz(tempTitle)
                sendToDatabase(docRef)
            }
            else{
                Toast.makeText(
                    this,
                    "Error",
                    Toast.LENGTH_LONG
                ).show()
            }
        }




    }

    private fun sendToDatabase(docRef: DocumentReference) {
        val docReference = database.collection(TEACHERS_QUIZ_PATH).document(newQuiz.quizId)
        docReference.
            /**
             * Reads the document referenced by this `DocumentReference`.
             *
             * @return A Task that will be resolved with the contents of the Document at this `DocumentReference`.
             */
        get(Source.DEFAULT)
            .addOnCompleteListener(OnCompleteListener<DocumentSnapshot?> { task ->
                if (task.isSuccessful) {
                    val doc = task.result
                    if (doc.exists()) {

                        Toast.makeText(
                            this,
                            "Failed to send Quiz, document already exists",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d(
                            "${TAG}-",
                            "Document already exists: ${docRef.id}"
                        )
                    } else {
                        Toast.makeText(this, "New quiz added", Toast.LENGTH_LONG).show()

                        Log.d(
                            "${TAG}-",
                            "Document ${docRef.id} does not exist!"
                        )
                        docReference.set(newQuiz)
                        makeInputFieldEmpty()
                    }
                }
            })
            .addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                makeInputFieldEmpty()
            }
    }

    private fun makeInputFieldEmpty() {
        title.setText("")

    }
     fun validation(tempTitle: String ): Boolean {
        Log.d("tempTitle", tempTitle)
         return if (tempTitle.matches(ALPHANUM.toRegex()) && tempTitle.length < LENGTHCHECK_50) true
         else{
             Toast.makeText(
                 this,
                 "Please enter a valid text or try decreasing text size",
                 Toast.LENGTH_LONG
             ).show()
             false
         }
//
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