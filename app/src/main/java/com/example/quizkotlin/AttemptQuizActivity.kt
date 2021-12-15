//package com.example.quizkotlin
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.RadioButton
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.databinding.DataBindingUtil
//import androidx.lifecycle.ViewModelProviders
//import com.example.quizkotlin.constants.SCORE
//import com.example.quizkotlin.databinding.ActivityAttemptQuizBinding
//import com.example.quizkotlin.models.Question
//import com.example.quizkotlin.viewmodels.QuestionViewModel
//import java.util.*
//import kotlin.collections.ArrayList
//
//class AttemptQuizActivity : AppCompatActivity() {
//    private lateinit var binding : ActivityAttemptQuizBinding
//    private var result : String = "highScore"
//    private var score : Int = 0
//    private var questionsList: List<Question> = ArrayList<Question>()
//    private var quid : Int = 0
//    private lateinit var currentQuestion : Question
//    private lateinit var questionViewModel: QuestionViewModel
//    private lateinit var answerrow1 : RadioButton
//    private lateinit var answerrow2 : RadioButton
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_attempt_quiz)
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
//        }
//    }
//    private fun setQuestionView(currentQuestion: Question) {
//        binding.questionTv.setText(currentQuestion.question)
//        binding.optionA.text = currentQuestion.optionA
//        binding.optionB.text = currentQuestion.optionB
//        binding.optionC.text = currentQuestion.optionC
//        binding.optionD.text = currentQuestion.optionD
//        quid += 1
//    }
//}