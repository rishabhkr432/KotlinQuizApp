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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.quizkotlin.activities.AddQuestion
import com.example.quizkotlin.R
import com.example.quizkotlin.activities.TeacherHomeActivity
import com.example.quizkotlin.constants
import com.example.quizkotlin.constants.INVALID_Q_TEXT
import com.example.quizkotlin.constants.SUCCESS
import com.example.quizkotlin.models.Quiz
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class AddQuestionFragment1 : Fragment() {

    companion object {
        fun newInstance() = AddQuestionFragment1()
    }


    private lateinit var question: EditText
    private lateinit var optionsNumber: EditText
    private var quizList: ArrayList<Quiz> = arrayListOf()
    private var quizListString: ArrayList<String> = arrayListOf()
    private lateinit var goBackButton: ImageView
    private lateinit var spinner: Spinner
    private lateinit var next_button: MaterialButton
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var spinnerSelectedText: String
    private  var quiz = Quiz()
    private lateinit var rv: RecyclerView
    private lateinit var ivClose: ImageView
    private lateinit var options_progress: ProgressBar
    private lateinit var optionFailed: TextView
    private var isValidation: Boolean = false
    lateinit var questionViewModel: QuestionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.question_fragment, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        user = auth.currentUser!!
        questionViewModel = ViewModelProvider(this)[QuestionViewModel::class.java]
        when(questionViewModel.getQuizzes()){
            SUCCESS -> {  Log.d(TAG, "quizList loaded")}}
//        database.collection("Quizzes").get()
//            .addOnSuccessListener {
//                if (it.isEmpty) {
//                    Toast.makeText(activity, "No quizzes found", Toast.LENGTH_SHORT).show()
//                } else {
//                    for (doc in it) {
//                        val quiz = doc.toObject(Quiz::class.java)
//                        quizList.add(quiz)
//                        quizListString.add(quiz.quizId)
////                        if (quiz.questionsForQuiz.size > 0) {
////                            for (i in quiz.questionsForQuiz) {
////                                questionsList.add(i)
////                            }
////                        }
//                        Log.d(TAG, quiz.toString())
//                    }
//                    getSpinnerValue(view)
//                }
//            }

        initViews(view)


        next_button.setOnClickListener {
            val questionTemp: String = question.text.toString().trim()
            val optionsTemp: Int = optionsNumber.text.toString().trim().toIntOrNull() ?: 4
            Log.d(TAG, "options: $optionsTemp")
            validations(
                questionTemp,
                optionsTemp
            )
            if (isValidation) {
                addOptions(questionTemp, optionsTemp)

            }
        }
        goBackButton.setOnClickListener {

            val intent = Intent(container?.context, TeacherHomeActivity::class.java)
            startActivity(intent)
            Log.d(TAG, "Opening Teacher's activity")

        }
//        questionViewModel.quizzesListString.observe(viewLifecycleOwner, Observer {
//                quiz ->
//            spinner.adapter =
//                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, quiz)
//        }
//        )
//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
////                val pos = parent?.getItemAtPosition(position)
//                quiz = questionViewModel.quizzesList[position]
////                quiz = parent?.getItemAtPosition(position) as Quiz
//                questionViewModel.quiz = quiz
////
//                Log.i(TAG,"QuizList selected is $quiz")  // <-- this works
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                // write code to perform some action
//            }
//        }
        return view
    }
private fun initViews(view: View) {
    question = view.findViewById(R.id.fragment_add_question)
    optionsNumber = view.findViewById(R.id.fragment_options)
    next_button = view.findViewById(R.id.fragment_next_button)
    goBackButton = view.findViewById(R.id.fragment_goBackButton)


//        rv = findViewById(R.id.options_list_rv)
//        optionFailed = findViewById(R.id.options_failed_tv)
//        options_progress = findViewById(R.id.options_progress)
//        rv.layoutManager = LinearLayoutManager(this)
//        rv.setHasFixedSize(true)
//        Resetting default value
//        optionsNumber.setText("4")
//        Log.i(TAG, optionsNumber.text.toString().trim())

    print("Info taken")


}
//    private fun getSpinnerValue(view: View) {
//        val spinner = view.findViewById<Spinner>(R.id.fragment_spinner)
//        spinnerSelectedText = quizList.first().quizId
//
//
//        if (spinner != null) {
//            val adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, quizListString)
//            spinner.adapter = adapter
//
//            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    parent: AdapterView<*>,
//                    view: View,
//                    position: Int,
//                    id: Long
//                ) {
//                    spinnerSelectedText = quizList[position].quizId
//                    println("QuizList selected is $spinnerSelectedText")  // <-- this works
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>) {
//                    // write code to perform some action
//                }
//            }
//        }
//
//// selectedText is not seen here:
//        Log.i(TAG,"quizList selected is $spinnerSelectedText")
////        return
//    }

    private fun validations(
        question_check: String,
        options: Int,

        ) {
        if (question_check != "" && question_check.isNotEmpty() && question_check.length > constants.LENGTHCHECK_5 && question_check.length < constants.LENGTHCHECK_100 && question_check.matches(
                constants.ALPHANUMQUESTION.toRegex())) {
            if (options in 1..10) {
                isValidation = true
                return
            } else {

                optionsNumber.error = "Please enter correct answer"
                optionsNumber.requestFocus()
                isValidation = false
                return
            }
        } else {
            question.error = INVALID_Q_TEXT
            question.requestFocus()
            isValidation = false
            return
        }
    }
    private fun addOptions(questionTemp: String, options: Int) {
        val questionFragment2 = AddQuestionFragment2()
        val args = Bundle()

        args.putString(AddQuestion.QUESTION, questionTemp)
//        args.putString(AddQuestion.SPINNER, spinnerText)
        args.putInt(AddQuestion.OPTIONS, options)
        questionFragment2.arguments = args
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.askQuestion, questionFragment2).addToBackStack(null)
            .commit()
    }


}