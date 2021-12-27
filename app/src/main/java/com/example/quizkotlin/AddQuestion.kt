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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizkotlin.models.Quiz
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.DocumentSnapshot

import com.google.android.gms.tasks.OnCompleteListener





class AddQuestion : AppCompatActivity() {
    private lateinit var question: EditText
    private lateinit var option1: EditText
    private lateinit var option2: EditText
    private lateinit var option3: EditText
    private lateinit var option4: EditText
    private lateinit var answer: EditText
    private lateinit var spinnerSelectedText: String

    private var creatingOptionsList: ArrayList<String?> = arrayListOf()
    private lateinit var addQuestionadapter: AddQuestionAdapter
    private var quizList: ArrayList<Quiz> = arrayListOf()
    private var quizListString: ArrayList<String> = arrayListOf()
    private lateinit var goBack: ImageView
    private lateinit var save_button: MaterialButton
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var rv: RecyclerView
    private lateinit var ivClose: ImageView
    private lateinit var options_progress: ProgressBar
    private lateinit var optionFailed: TextView
    private var MAXOPTIONS: Int = 1
    private var isValidation: Boolean = false

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
                        Log.d(TAG, quiz.toString())
                    }
                    getSpinnerValue()
                }
            }

        initViews()


        save_button.setOnClickListener {

            Log.d("correct_answer", answer.text.toString())
            Log.d("optionList", creatingOptionsList.toString())
//            saveClassToDatabase

            var question_temp: String = question.text.toString().trim()
            var optionList: ArrayList<String?> = creatingOptionsList
//        var optionB : String = option2.text.toString().trim()
//        var optionC : String = option3.text.toString().trim()
//        var optionD : String = option4.text.toString().trim()
            var correctAnswer = answer.text.toString().trim()
            validations(question_temp, optionList, correctAnswer)
            if (isValidation == true) {
//                questionsList.add(Question(question_temp, optionList, correctAnswer))
            saveClassToDatabase(question_temp, optionList, correctAnswer)
            }



            goBack.setOnClickListener {
                TeacherHomeActivity.teachershomeActivity.finish()
                val intent = Intent(this, TeacherHomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
    private fun saveClassToDatabase(question_name: String, optionsList: ArrayList<String?>, correctAnswer: String ) {
//        validations(question_name, optionA, optionB, optionC, optionD, correctAnswer)
//
//
//            if (isValidation == true) {
                val newQuestion = Question(
                    question_name,
                    optionsList,
                    correctAnswer
                )
                database.collection("Quizzes")
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            if (document.get("id") == (spinnerSelectedText)) {
////
                                val quiz = (document.toObject(Quiz::class.java))
                                val quizSize= quiz.questionsForQuiz.size
                                Log.d(TAG, quiz.toString())
//
//
                                if (quizSize < 10){
//
                                    document.reference.update(
                                        "questionsForQuiz",
                                        FieldValue.arrayUnion(newQuestion)

                                    )
                                        .addOnSuccessListener {
                                            Toast.makeText(this,
                                                "Added, Total questions in the quiz: $quizSize", Toast.LENGTH_LONG).show()
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
//
                                            finish();
                                            this.recreate();
                                        }
                                }

                                else {
                                    if (quizSize == 10) {
                                        Toast.makeText(
                                            this,
                                            "Cannot add more questions to this quiz. Current size: $quizSize",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        sendQuiztoStudentDatabase(document, quiz)
                                    } else {


                                        Toast.makeText(
                                            this,
                                            "Cannot add more questions to this quiz. Current size: $quizSize",
                                            Toast.LENGTH_LONG
                                        ).show()

                                    }
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
//    }


        private fun initViews() {
            question = findViewById(R.id.add_question)
//        option1 = findViewById(R.id.option1)
//        option2 = findViewById(R.id.option2)
//        option3 = findViewById(R.id.option3)
//        option4 = findViewById(R.id.option4)
            answer = findViewById(R.id.correct_answer_rv)
            save_button = findViewById(R.id.save_button)
            goBack = findViewById(R.id.goBackButton)
            rv = findViewById(R.id.options_list_rv)
            optionFailed = findViewById(R.id.options_failed_tv)
            options_progress = findViewById(R.id.options_progress)
            rv.layoutManager = LinearLayoutManager(this)
            rv.setHasFixedSize(true)
            passedQuestionandSpinner()
            getOptions()


        }

        //    private fun createQuestion(){
//        val q = new Question("1",
//        question,
//        creatingOptionsList
//        )
//    }
        private fun getOptions() {
            for (i in 1..MAXOPTIONS) {
                creatingOptionsList.add("")
            }
//    creatingOptionsList = ArrayList(initialCapacity = MAXOPTIONS)

            Log.d("creatingOptionsList", creatingOptionsList.toString())
            if (creatingOptionsList.isEmpty()) {
                options_progress.visibility = View.GONE
                optionFailed.visibility = View.VISIBLE
            } else {
                optionFailed.visibility = View.GONE
                addQuestionadapter = AddQuestionAdapter(
                    creatingOptionsList,
                    this,
                    object : AddQuestionAdapter.MyViewHolder.Listener {
                        override fun optionsReturn(list: ArrayList<String?>) {
                            Log.d(TAG, "optionsList returned : $list")

                        }
                    })
                rv.adapter = addQuestionadapter
            }

        }

        private fun getSpinnerValue() {
            val spinner = findViewById<Spinner>(R.id.spinner)
            spinnerSelectedText = quizList.first().id


            if (spinner != null) {
                val adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item, quizListString)
                spinner.adapter = adapter

                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
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

        private fun passedQuestionandSpinner() {
            var questionPassed = intent.getStringExtra(QUESTION) ?: return
            var spinnerPassed = intent.getStringExtra(SPINNER) ?: return
            var options = intent.getIntExtra(OPTIONS, 1)
            question.setText(questionPassed)
            spinnerSelectedText = spinnerPassed
            MAXOPTIONS = options
            Log.i("question", questionPassed)
            Log.i("spinnerSelectedText", spinnerPassed)
            Log.i("options", options.toString())
//        if (questionPassed != null) {
//            question.setText(questionPassed)
//            Log.i(TAG, questionPassed)
//        }
//
//
//        if (spinnerPassed != null) {
//            spinnerSelectedText = spinnerPassed
//            Log.i(TAG, spinnerPassed)
//        }


        }

        private fun validations(
            question_check: String,
            temp_optionList: ArrayList<String?>,
            correctAnswer: String
        ) {
//        if (question_set != "" && !question_set.isEmpty()) {
            if (question_check != "" && !question_check.isEmpty()) {
                for (i in temp_optionList) {
                    if (i != null) {
                        if (i != "" && !i.isEmpty()) {
                            if ((correctAnswer != "" && !correctAnswer.isEmpty()) && temp_optionList.contains(
                                    correctAnswer
                                )
                            ) {
                                isValidation = true
                                return
                            } else {
                                answer.error = "Please enter correct answer"
                                answer.requestFocus()
                                isValidation = false
                                return
                            }
                        } else {
                //                        creatingOptionsList.error = "Please enter option D"
                //                        creatingOptionsList.requestFocus()
                            isValidation = false
                            return
                        }
                    }
                }
            } else {
                question.error = "Please enter question"
                question.requestFocus()
                isValidation = false
                return
            }
        }

//                if (optionA != "" && !optionA.isEmpty()) {
//                    if (optionB != "" && !optionB.isEmpty()) {
//                        if (optionC != "" && !optionC.isEmpty()) {
//                            if (optionD != "" && !optionD.isEmpty()) {
//                                if ((correctAnswer != "" && !correctAnswer.isEmpty()) && correctAnswer == optionA || correctAnswer == optionB || correctAnswer == optionC || correctAnswer == optionD)  {
//                                    isValidation = true
//                                    return
//                                } else {
//                                    answer.error = "Please enter correct answer"
//                                    answer.requestFocus()
//                                    isValidation = false
//                                    return
//                                }
//                            } else {
//                                option4.error = "Please enter option D"
//                                option4.requestFocus()
//                                isValidation = false
//                                return
//                            }
//                        } else {
//                            option3.error = "Please enter option C"
//                            option3.requestFocus()
//                            isValidation = false
//                            return
//                        }
//                    } else {
//                        option2.error = "Please enter option B"
//                        option2.requestFocus()
//                        isValidation = false
//                        return
//                    }
//                } else {
//                    option1.error = "Please enter option A"
//                    option1.requestFocus()
//                    isValidation = false
//                    return
//                }
//            } else {
//                question.error = "Please enter question"
//                question.requestFocus()
//                isValidation = false
//                return
//            }
//        }

        private fun sendQuiztoStudentDatabase(quid: QueryDocumentSnapshot, quiz: Quiz) {
            val docRef = database.collection("Student's quizzes").document(quid.id)
            Log.d(TAG, quid.id)
            docRef.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot?> { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!")
                        Toast.makeText(
                            this,
                            "Failed to send Quiz, document already exists",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        Log.d(TAG, "Document does not exist!")
                        val save = (quid.toObject(Quiz::class.java))
                        docRef.set(save)
//                    docRef.set((quid.id.toObject(Quiz::class.java))
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "Quiz send to the student database",
                                    Toast.LENGTH_LONG
                                ).show()
//                            makeInputFieldEmpty()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to send quiz", Toast.LENGTH_LONG)
                                    .show()
//                            makeInputFieldEmpty()
                            }

                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.exception)
                }
            })


                .addOnSuccessListener {
                    Toast.makeText(this, "Quiz send to student database", Toast.LENGTH_LONG).show()

                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to send Quiz", Toast.LENGTH_LONG).show()

                }
        }
        init {
            addquestionActivity = this
        }
        companion object {
            @SuppressLint("StaticFieldLeak")
            private const val TAG = "AddQuestion"
            lateinit var addquestionActivity: Activity
            var QUESTION = "Question"
            var SPINNER = "Spinner"
            var OPTIONS = "Options"
        }




    private operator fun Boolean.invoke(value: Any) {

    }
}

