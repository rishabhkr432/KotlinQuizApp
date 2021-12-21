package com.example.quizkotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.quizkotlin.models.Question
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Intent
import android.view.View
import android.widget.*
import com.example.quizkotlin.models.Quiz
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QueryDocumentSnapshot


class AddQuestion : AppCompatActivity(){
    private lateinit var question: EditText
    private lateinit var option1: EditText
    private lateinit var option2: EditText
    private lateinit var option3: EditText
    private lateinit var option4: EditText
    private lateinit var answer: EditText
    private lateinit var spinnerSelectedText: String
//    private var questionsList: ArrayList<Question> = arrayListOf()
    private var quizList: ArrayList<Quiz> = arrayListOf()
    private var quizListString: ArrayList<String> = arrayListOf()
    private lateinit var goBack: ImageView
    private lateinit var save_button: MaterialButton
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
//    private lateinit var counter: Int
    private var isValidation : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_question)
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        user = auth.currentUser!!
        database.collection("Quizzes").get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    Toast.makeText(this, "No quizzes found", Toast.LENGTH_SHORT).show()
                } else {
                    for (doc in it) {
                        val quiz = doc.toObject(Quiz::class.java)
                        quizList.add(quiz)
                        quizListString.add(quiz.id)
//                        if (quiz.questionsForQuiz.size > 0) {
//                            for (i in quiz.questionsForQuiz) {
//                                questionsList.add(i)
//                            }
//                        }
                        Log.d(TAG, quiz.toString())
                    }
                    getSpinnerValue()
                }
            }

        initViews()


        save_button.setOnClickListener {
            print("button clicked")
//            saveClassToDatabase

        var question_temp : String = question.text.toString().trim()
        var optionA : String = option1.text.toString().trim()
        var optionB : String = option2.text.toString().trim()
        var optionC : String = option3.text.toString().trim()
        var optionD : String = option4.text.toString().trim()
        var correctAnswer = answer.text.toString().trim()
//            validations(question_temp, optionA, optionB, optionC, optionD, correctAnswer)
//            if (isValidation == true) {
//                questionsList.add(Question(question_temp,optionA,optionB,optionC,optionD,correctAnswer))
            saveClassToDatabase(question_temp,optionA,optionB,optionC,optionD,correctAnswer)
            }



        goBack.setOnClickListener {
            TeacherHomeActivity.teachershomeActivity.finish()
            val intent = Intent(this, TeacherHomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun saveClassToDatabase(question_name: String, optionA: String, optionB: String, optionC: String, optionD: String, correctAnswer: String ) {
        validations(question_name, optionA, optionB, optionC, optionD, correctAnswer)
        val upperlimit = 10

            if (isValidation == true) {
                val newQuestion = Question(
                    "1",
                    question_name,
                    optionA,
                    optionB,
                    optionC,
                    optionD,
                    correctAnswer
                )
                database.collection("Quizzes").get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            if (document.get("id") == (spinnerSelectedText)) {
//                                val array = document.get("questionsForQuiz")
                                val array = (document.toObject(Quiz::class.java).questionsForQuiz.size)
                                Log.d(TAG, array.toString())


                                if (array < 10){

                                    document.reference.update(
                                        "questionsForQuiz",
                                        FieldValue.arrayUnion(newQuestion)

                                    )
                                        .addOnSuccessListener {
                                            Toast.makeText(this, "Added, Total questions: "+ array, Toast.LENGTH_LONG).show()
                                            Log.d(
                                                TAG,
                                                "Question successfully added!"
                                            )

                                            finish();
                                            overridePendingTransition(0, 0);
                                            startActivity(intent);
                                            overridePendingTransition(0, 0);
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                                            Log.w(
                                                TAG,
                                                "Error adding question",
                                                e
                                            )

                                            finish();
                                            this.recreate();
                                        }
                                }
                                else{
                                    Toast.makeText(this, "Cannot add more to this quiz", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }


//                washingtonRef.set(newQuestion)
//                    .addOnSuccessListener {
//                        Toast.makeText(this, "Created", Toast.LENGTH_LONG).show()
//                        Log.d(TAG, "Created")
//                        finish();
//                        overridePendingTransition(0, 0);
//                        startActivity(intent);
//                        overridePendingTransition(0, 0);
//                    }
//                    .addOnFailureListener { exception ->
//                        Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
//                        finish();
////                this.recreate();
//                        Log.d(TAG, "get failed with ", exception)
//                    }
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
        goBack = findViewById(R.id.goBackButton)
//        getSpinnerValue()

        print("Info taken")


    }
    private fun getSpinnerValue(){
        val spinner = findViewById<Spinner>(R.id.spinner)
        spinnerSelectedText = quizList.first().id


        if (spinner != null) {
            val adapter = ArrayAdapter( this, android.R.layout.simple_spinner_item, quizListString)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    spinnerSelectedText = quizList[position].id
                    println("QuizList selected is $spinnerSelectedText")  // <-- this works
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

// selectedText is not seen here:
        println("quizList selected is $spinnerSelectedText")
//        return
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
                                if ((correctAnswer != "" && !correctAnswer.isEmpty()) && correctAnswer == optionA || correctAnswer == optionB || correctAnswer == optionC || correctAnswer == optionD)  {
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
    init {
        addquestionActivity = this
    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        private const val TAG = "AddQuestion"
        lateinit var addquestionActivity: Activity
    }

}


private operator fun Boolean.invoke(value: Any) {

}
