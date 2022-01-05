package com.example.quizkotlin.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizkotlin.AddQuestion
import com.example.quizkotlin.R
import com.example.quizkotlin.TeacherHomeActivity
import com.example.quizkotlin.adapters.AddQuestionAdapter
import com.example.quizkotlin.constants
import com.example.quizkotlin.constants.ALPHANUM
import com.example.quizkotlin.constants.LENGTHCHECK_100
import com.example.quizkotlin.constants.LENGTHCHECK_50
import com.example.quizkotlin.constants.QUIZ_ID
import com.example.quizkotlin.constants.QUIZ_QUESTIONS
import com.example.quizkotlin.constants.TEACHERS_QUIZ_PATH
import com.example.quizkotlin.models.Question
import com.example.quizkotlin.models.Quiz
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore





class AddQuestionFragment2 : Fragment() {

    companion object {
        fun newInstance() = AddQuestionFragment2()
        var QUESTION = "Question"
        var SPINNER = "Spinner"
        var OPTIONS = "Options"
    }

    private lateinit var question: EditText
    private lateinit var answer: EditText
    private lateinit var spinnerSelectedText: String
    private lateinit var spinner: Spinner
    private var creatingOptionsList: ArrayList<String?> = arrayListOf()
    private lateinit var addQuestionadapter: AddQuestionAdapter
    private var quizList: ArrayList<Quiz> = arrayListOf()
    private var quizListString: ArrayList<String> = arrayListOf()
    private  var quiz = Quiz()
    private lateinit var goBackButton: ImageView
    private lateinit var save_button: MaterialButton
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var rv: RecyclerView
    private lateinit var options_progress: ProgressBar
    private lateinit var optionFailed: TextView
    private var minOptions: Int = 1
    private var isValidation: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.question_fragment2, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        user = auth.currentUser!!

        database.collection(TEACHERS_QUIZ_PATH).get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    Toast.makeText(container?.context, "No quizzes found", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    for (doc in it) {
                        val quiz = doc.toObject(Quiz::class.java)
                        quizList.add(quiz)
                        quizListString.add(quiz.quizId)
                        Log.d(TAG, quiz.toString())
                    }
                    getSpinnerValue(view)
                }
            }


        initViews(view)

        save_button.setOnClickListener {

            Log.d("QuizTitle - ", quiz.quizId)
            Log.d("correct_answer", answer.text.toString())
            Log.d("optionList size", creatingOptionsList.toString())

            val questionTemp: String = question.text.toString().trim()
            val optionList: ArrayList<String?> = creatingOptionsList
            val correctAnswer = answer.text.toString().trim()


            validations(questionTemp, optionList, correctAnswer,view)
            if (isValidation) {
                saveClassToDatabase(questionTemp, optionList, correctAnswer, view)

//                    SUCCESS -> Toast.makeText(
//                                        view.context,
//                                        "Question added",
//                                        Toast.LENGTH_LONG
//                                    ).show()
//                    FAILED -> Toast.makeText(view.context, "Failed to add questions", Toast.LENGTH_LONG).show()
//                OVERLOADED -> Toast.makeText(view.context,"Cannot add more questions to this quiz.", Toast.LENGTH_LONG).show()
//                    NULLCHECK -> Log.i(TAG, "loaded")
//                    else -> Toast.makeText(view.context, "${quiz.quizId} not found", Toast.LENGTH_LONG).show()
//                }
            }

        }



        goBackButton.setOnClickListener {

            val intent = Intent(container?.context, TeacherHomeActivity::class.java)
            startActivity(intent)
            Log.d(TAG, "Opening Teacher's activity")

        }
        return view
    }

    private fun saveClassToDatabase(
        question_name: String,
        optionsList: ArrayList<String?>,
        correctAnswer: String,
        view: View
    ) {

        val newQuestion = Question(

            question_name,
            optionsList,
            correctAnswer
        )
        database.collection(TEACHERS_QUIZ_PATH)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if (document.get(QUIZ_ID) == (spinnerSelectedText)) {
////
                        val quiz = (document.toObject(Quiz::class.java))
                        val quizSize = quiz.questionsForQuiz.size
                        Log.d(TAG, quiz.toString())
//
//
                        if (quizSize < 10) {
//
                            document.reference.update(
                                QUIZ_QUESTIONS,
                                FieldValue.arrayUnion(newQuestion)

                            )
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        view.context,
                                        "Added, Total questions in the quiz: ${quizSize + 1}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    Log.d(
                                        TAG,
                                        "Question successfully added!"
                                    )

                                    (requireActivity() as AddQuestion).restartFragment(R.id.askQuestion)
                                    makeInputFieldEmpty()




                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(view.context, "Failed", Toast.LENGTH_LONG).show()
                                    Log.w(
                                        TAG,
                                        "Error adding question",
                                        e
                                    )
//

                                }
                        } else {
                            Toast.makeText(
                                view.context,
                                "Cannot add more questions to this quiz. Current size: $quizSize",
                                Toast.LENGTH_LONG
                            ).show()

                        }

                    }
                    else{
                        Log.i(TAG,"Quiz not found $spinnerSelectedText")
                    }
                }
            }

    }

    private fun makeInputFieldEmpty() {
        question.setText("")
        creatingOptionsList.clear()
        answer.setText("")

    }


    private fun initViews(view: View) {
        question = view.findViewById(R.id.add_question)
        answer = view.findViewById(R.id.correct_answer_rv)
        save_button = view.findViewById(R.id.save_button)
        goBackButton = view.findViewById(R.id.add_question_goBackButton)
        rv = view.findViewById(R.id.options_list_rv)
        optionFailed = view.findViewById(R.id.options_failed_tv)
        options_progress = view.findViewById(R.id.options_progress)
        rv.layoutManager = LinearLayoutManager(view.context)
        rv.setHasFixedSize(true)
        passedQuestionandSpinner()
        getOptions(view)

    }

    private fun getOptions(view: View) {

        for (i in 1..minOptions) {
            creatingOptionsList.add("")
        }
//        if (creatingOptionsList.size == 1){
//
//        }

        Log.d("creatingOptionsList", creatingOptionsList.toString())
        if (creatingOptionsList.isEmpty()) {

            options_progress.visibility = View.GONE
            optionFailed.visibility = View.VISIBLE
        } else if(creatingOptionsList.size > 1) {

            optionFailed.visibility = View.GONE
            addQuestionadapter = AddQuestionAdapter(
                creatingOptionsList,
                view.context,
                object : AddQuestionAdapter.MyViewHolder.Listener {
                    override fun optionsReturn(list: ArrayList<String?>) {

                        Log.d(TAG, "optionsList returned : ${list}")
                        Log.d(TAG, "optionsList size : ${list.size}")
                        Log.d(TAG, "optionsList creatingOptionsList : ${creatingOptionsList.toString()}")


                    }
                })
            rv.adapter = addQuestionadapter
        } else{
            rv.visibility = View.GONE
        }

    }

    private fun getSpinnerValue(view: View) {
        val spinner = view.findViewById<Spinner>(R.id.spinner)
            spinnerSelectedText = quizList.first().quizId

        if (spinner != null) {
            val adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, quizListString)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    spinnerSelectedText = quizList[position].quizId
                    println("QuizList selected is $spinnerSelectedText")  // <-- this works
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

// selectedText is not seen here:
        Log.d("$TAG- getSpinnerValue() -", "quizList selected is $spinnerSelectedText")
//        return
    }

    private fun passedQuestionandSpinner() {
        val questionPassed = arguments?.getString(QUESTION)
        val spinnerPassed = arguments?.getString(SPINNER)
        val options = arguments?.getInt(OPTIONS, 1)

        if (questionPassed != null) {
            question.setText(questionPassed)
            Log.d("$TAG- passedQuestionandSpinner - question", questionPassed)
        }
        if (spinnerPassed != null) {
            spinnerSelectedText = spinnerPassed
            Log.d("$TAG- passedQuestionandSpinner - spinnerSelectedText", spinnerPassed)
        }
        if (options != null) {
            minOptions = options
            Log.d("$TAG- passedQuestionandSpinner - options", options.toString())
        }



    }

    private fun validations(
        question_check: String,
        temp_optionList: ArrayList<String?>,
        correctAnswer: String,
        view: View
    ) {
        var validateOptions = 0
        Log.i(TAG,"${temp_optionList.toSet()}  $temp_optionList")
        if (question_check != "" && question_check.isNotEmpty() && question_check.length < LENGTHCHECK_100 && question_check.matches(ALPHANUM.toRegex())) {
            if (temp_optionList.toSet().size == temp_optionList.size) {
                for (i in temp_optionList) {
                    if (i != null) {
                        if(i == "" && temp_optionList.size == 1) {
                            if (correctAnswer != "") {
                                isValidation = true

                                return
                            } else {
                                answer.error = "Please make sure answer box is not blank"
                                answer.requestFocus()
                                isValidation = false
                                return
                            }
                        }
                        else if (i != "" && i.isNotEmpty() && temp_optionList.size>1 && i.length < LENGTHCHECK_50 && i.matches(ALPHANUM.toRegex())) {
                            if (correctAnswer != "" && correctAnswer.length < LENGTHCHECK_50 && correctAnswer.matches(
                                    ALPHANUM.toRegex()) && (correctAnswer.isNotEmpty() && temp_optionList.contains(
                                    correctAnswer
                                ))
                            ) {
                                validateOptions += 1
                                if (validateOptions == minOptions) {
                                    isValidation = true

                                    return
                                }
                            } else {
                                answer.error = "Please enter correct answer"
                                answer.requestFocus()
                                isValidation = false
                                return
                            }
                        }
//                        else if(i == "" && temp_optionList.size == 1){
//                            if (correctAnswer == ""){
//                                isValidation = true
//
//                                return
//                            }
//                            else {
//                                answer.error = "Please make sure answer box is blank"
//                                answer.requestFocus()
//                                isValidation = false
//                                return
//                            }
//
//
                         else{
                            //                        creatingOptionsList.error = "Please enter option D"
                            //                        creatingOptionsList.requestFocus()
                            Toast.makeText(
                                view.context,
                                "Please enter valid option for the quiz. \nFor 1 word answers leave options blank",
                                Toast.LENGTH_SHORT
                            ).show()
                            isValidation = false
                            return
                        }
                    }
                }
            }
            else{
                Toast.makeText(
                    view.context,
                    "Please make sure there are no duplicates in the options",
                    Toast.LENGTH_SHORT
                ).show()
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

}