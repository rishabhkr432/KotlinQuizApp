package com.example.quizkotlin;
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.quizkotlin.*
import com.example.quizkotlin.models.Question
import com.example.quizkotlin.models.Quiz
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class AttemptQuizActivity : AppCompatActivity() {
    private var result : String = "highScore"
    private var score : Int = 0
    private var questionsList: List<Question> = ArrayList()
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var next_button: MaterialButton
    private lateinit var question: TextView
    private lateinit var qnum_display: TextView
    private lateinit var option1: RadioButton
    private lateinit var option2: RadioButton
    private lateinit var option3: RadioButton
    private lateinit var option4: RadioButton
    private lateinit var quizPas: Quiz
    private var qNum : Int = 0
    private lateinit var goBack: ImageView
    private lateinit var currentQuestion : Question
    var radioGroup: RadioGroup? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attempt_quiz)


        quizPas = intent.getSerializableExtra(QUIZ_PASS) as Quiz
        initViews()
        questionsList = quizPas.questionsForQuiz
        Collections.shuffle(questionsList)
        currentQuestion = questionsList[qNum]
        setCurrentQuestion(currentQuestion)

        goBack.setOnClickListener {
            StudentHomeActivity.studenthomeActivity.finish()
            val intent = Intent(this, StudentHomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        next_button.setOnClickListener {
            radioGroup?.clearCheck()

            if (currentQuestion.correct_answer.equals(option1.text)){
                score+=1

            }else if (currentQuestion.correct_answer.equals(option2.text)) {
                score += 1
            }else if (currentQuestion.correct_answer.equals(option3.text)){
                    score+=1
            }else if (currentQuestion.correct_answer.equals(option4.text)){
                    score+=1
            }else{
                Toast.makeText(this, "Answer is wrong", Toast.LENGTH_SHORT)
                    .show()
            }
            Toast.makeText(this, "Next question", Toast.LENGTH_SHORT)
                .show()


            if (qNum < questionsList.size) {
                currentQuestion = questionsList[qNum]
                setCurrentQuestion(currentQuestion)
            } else {
                val sharedPreferences = getSharedPreferences("Result", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt(result, score)
                editor.commit()
                constants.SCORE = score
                val intent = Intent(this, StudentHomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
        //
//        questionViewModel = ViewModelProviders.of(this).get(QuestionViewModel::class.java)
//        questionViewModel.getAllQuestionList().observe(this, {
//            questionsList = it as ArrayList<Question>
//        })
//        Collections.shuffle(questionsList)
//        currentQuestion = questionsList[quid]
//        setQuestionView(currentQuestion)
//
//        binding.next.setOnClickListener {
//            answerrow1 = binding.radioGroupRow1.checkedRadioButtonId as RadioButton
//            answerrow2 = binding.radioGroupRow2.checkedRadioButtonId as RadioButton
//            if (currentQuestion.correctAnswer.equals(answerrow1.text)){
//                score+=1
//            }else if (currentQuestion.correctAnswer.equals(answerrow2.text)){
//                score+=1
//            }else{
//                Toast.makeText(this@AttemptQuizActivity, "Answer is wrong", Toast.LENGTH_SHORT)
//                    .show()
//            }
//            if (quid<questionsList.size){
//                currentQuestion =questionsList[quid]
//                setQuestionView(currentQuestion)
//            }else{
//                val sharedPreferences = getSharedPreferences("Result", MODE_PRIVATE)
//                val editor = sharedPreferences.edit()
//                editor.putInt(result, score)
//                editor.commit()
//                SCORE = score
//                val intent = Intent(applicationContext, StudentHomeActivity::class.java)
//                startActivity(intent)
//                finish()
//            }


        private fun initViews() {
            qnum_display = findViewById(R.id.qnum_display)
            question = findViewById(R.id.question_attempt)
            option1 = findViewById(R.id.student_option1_attempt) as RadioButton
            option2 = findViewById(R.id.student_option2_attempt)
            option3 = findViewById(R.id.student_option3_attempt)
            option4 = findViewById(R.id.student_option4_attempt)
            radioGroup = findViewById(R.id.radioGrp)
            next_button = findViewById(R.id.next_button_demo)

            goBack = findViewById(R.id.goBackButton_quiz_attempt)
        }

        private fun setCurrentQuestion(currentQuestion: Question) {
            question.text = currentQuestion.question
            option1.text = currentQuestion.option1
            option2.text = currentQuestion.option2
            option3.text = currentQuestion.option3
            option4.text = currentQuestion.option4
            qNum += 1
            qnum_display.text = ((qNum+1).toString() + "/10")

        }
    companion object {
        @SuppressLint("StaticFieldLeak")
        var QUIZ_PASS = "Quiz"
    }

}