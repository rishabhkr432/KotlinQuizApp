package com.example.quizkotlin

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizkotlin.models.Question
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class AddQuestion : AppCompatActivity(){
    private lateinit var question: EditText
    private lateinit var option1: EditText
    private lateinit var option2: EditText
    private lateinit var option3: EditText
    private lateinit var option4: EditText
    private lateinit var answer: EditText
    private var questionsList: ArrayList<Question> = arrayListOf()

    private lateinit var save_button: MaterialButton
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private var isValidation : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_question)

        initViews()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        user = auth.currentUser!!

        save_button.setOnClickListener {
            print("button clicked")
            var question_temp : String = question.text.toString().trim()
            var optionA : String = option1.text.toString().trim()
            var optionB : String = option2.text.toString().trim()
            var optionC : String = option3.text.toString().trim()
            var optionD : String = option4.text.toString().trim()
            var correctAnswer = answer.text.toString().trim()
            validations(question_temp, optionA, optionB, optionC, optionD, correctAnswer)
            if (isValidation == true) {
                questionsList.add(Question(question_temp,optionA,optionB,optionC,optionD,correctAnswer))
                saveClassToDatabase(Question(question_temp,optionA,optionB,optionC,optionD,correctAnswer))
            }


        }
    }
    private fun saveClassToDatabase(question_save: Question ) {
        val docRef = database.collection("Questions").document()



        docRef.set(question_save)
            .addOnSuccessListener {
                Toast.makeText(this, "Created", Toast.LENGTH_LONG).show()
                finish();
                this.recreate();
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                finish();
                this.recreate();
                Log.d(TAG, "get failed with ", exception)
            }
    }


    private fun initViews() {
        question = findViewById(R.id.add_question)
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)
        answer = findViewById(R.id.correct_answer)
        save_button = findViewById(R.id.save_button)
        print("Info taken")


    }
    private fun validations(
        question_check: String,
//        question_set: String,
        optionA: String,
        optionB: String,
        optionC: String,
        optionD: String,
        correctAnswer: String
    ) {
//        if (question_set != "" && !question_set.isEmpty()) {
            if (question_check != "" && !question_check.isEmpty()) {
                if (optionA != "" && !optionA.isEmpty()) {
                    if (optionB != "" && !optionB.isEmpty()) {
                        if (optionC != "" && !optionC.isEmpty()) {
                            if (optionD != "" && !optionD.isEmpty()) {
                                if (correctAnswer != "" && !correctAnswer.isEmpty()) {
                                    isValidation = true
                                    return
                                } else {
                                    answer.error = "Please enter correct answer"
                                    answer.requestFocus()
                                    isValidation = false
                                    return
                                }
                            } else {
                                option4.error = "Please enter option D"
                                option4.requestFocus()
                                isValidation = false
                                return
                            }
                        } else {
                            option3.error = "Please enter option C"
                            option3.requestFocus()
                            isValidation = false
                            return
                        }
                    } else {
                        option2.error = "Please enter option B"
                        option2.requestFocus()
                        isValidation = false
                        return
                    }
                } else {
                    option1.error = "Please enter option A"
                    option1.requestFocus()
                    isValidation = false
                    return
                }
            } else {
                question.error = "Please enter question"
                question.requestFocus()
                isValidation = false
                return
            }
        }
    companion object {
        private const val TAG = "AddQuestion"
    }

}