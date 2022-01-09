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
import androidx.lifecycle.ViewModelProvider
import com.example.quizkotlin.activities.AddQuestion
import com.example.quizkotlin.R
import com.example.quizkotlin.activities.TeacherHomeActivity
import com.example.quizkotlin.Constants
import com.example.quizkotlin.Constants.INVALID_Q_TEXT
import com.example.quizkotlin.Constants.SUCCESS
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
    private lateinit var goBackButton: ImageView
    private lateinit var next_button: MaterialButton
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private var isValidation: Boolean = false
    lateinit var questionViewModel: QuestionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        user = auth.currentUser!!
        questionViewModel = ViewModelProvider(this)[QuestionViewModel::class.java]
        when(questionViewModel.getQuizzes()){
            SUCCESS -> {  Log.d(TAG, "quizList loaded")}}

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

        return view
    }
    /**
     * Setting views
     */
private fun initViews(view: View) {
    question = view.findViewById(R.id.fragment_add_question)
    optionsNumber = view.findViewById(R.id.fragment_options)
    next_button = view.findViewById(R.id.fragment_next_button)
    goBackButton = view.findViewById(R.id.fragment_goBackButton)


    print("Info taken")


}

    /**
     * Errorchecking.
     */
    private fun validations(
        question_check: String,
        options: Int,

        ) {
        if (question_check != "" && question_check.isNotEmpty() && question_check.length > Constants.LENGTHCHECK_5 && question_check.length < Constants.LENGTHCHECK_100 && question_check.matches(
                Constants.ALPHANUMQUESTION.toRegex())) {
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
    /**
     * passing parameters to the next fragment.
     */
    private fun addOptions(questionTemp: String, options: Int) {
        val questionFragment2 = AddQuestionFragment2()
        val args = Bundle()

        args.putString(AddQuestion.QUESTION, questionTemp)
        args.putInt(AddQuestion.OPTIONS, options)
        questionFragment2.arguments = args
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.askQuestion, questionFragment2).addToBackStack(null)
            .commit()
    }


}