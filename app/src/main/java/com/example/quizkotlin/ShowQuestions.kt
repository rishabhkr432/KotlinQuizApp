package com.example.quizkotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizkotlin.models.Question
import com.example.quizkotlin.models.Quiz
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class ShowQuestions() : AppCompatActivity() {
//    private var score : Int = 0
//    private var questionsList: List<Question> = ArrayList<Question>()
//    private lateinit var database: FirebaseFirestore
//    private lateinit var auth: FirebaseAuth
//    private lateinit var user: FirebaseUser
//    private lateinit var question: EditText
//    private lateinit var option1: EditText
//    private lateinit var option2: EditText
//    private lateinit var option3: EditText
//    private lateinit var option4: EditText
//    private lateinit var answer: EditText
//    private lateinit var goBack: ImageView
//    private lateinit var delete_button: MaterialButton
//    private lateinit var next_button: MaterialButton
//    private lateinit var quizPas: Quiz
//    private var q_num : Int = 0
//    private lateinit var currentQuestion : Question
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.dummy_quiz)
//
//
//
//        auth = FirebaseAuth.getInstance()
//        database = FirebaseFirestore.getInstance()
//        user = auth.currentUser!!
//        quizPas = intent.getSerializableExtra(QUIZ_PASS) as Quiz
//        initViews()
//        questionsList = quizPas.questionsForQuiz
//        Collections.shuffle(questionsList)
//        currentQuestion = questionsList[q_num]
//        setCurrentQuestion(currentQuestion)
//
//        goBack.setOnClickListener {
//            TeacherHomeActivity.teachershomeActivity.finish()
//            val intent = Intent(this, TeacherHomeActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//        next_button.setOnClickListener {
//
//
//
//
//            if (q_num<questionsList.size) {
//                currentQuestion = questionsList[q_num]
//                setCurrentQuestion(currentQuestion)
//            }
//            else{
//                val intent = Intent(this, TeacherHomeActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//
//
//
//        }
//        delete_button.setOnClickListener {
//
//
//        }
//
//
//
//
//
////    private fun getQuestions() {
////
////
////    }
//
//
////        for (i in questionsList){
////            question.setText(i.question)
////            option1.setText(i.option1)
////            option2.setText(i.option2)
////            option3.setText(i.option3)
////            option4.setText(i.option4)
////            answer.setText(i.correct_answer)
////        }
//
//
//
//
//    }
//
//
//    private fun initViews() {
//
//            question = findViewById(R.id.show_question_attempt)
//            option1 = findViewById(R.id.show_option1_attempt)
//            option2 = findViewById(R.id.show_option2_attempt)
//            option3 = findViewById(R.id.show_option3_attempt)
//            option4 = findViewById(R.id.show_option4_attempt)
//            answer = findViewById(R.id.show_correct_answer_attempt)
//            delete_button = findViewById(R.id.delete_button_demo)
//            next_button = findViewById(R.id.next_button_demo)
//
//            goBack = findViewById(R.id.goBackButton_attempt)
//    }
//private fun setCurrentQuestion(currentQuestion: Question) {
//    question.setText(currentQuestion.question)
//    option1.setText(currentQuestion.option1)
//    option2.setText(currentQuestion.option2)
//    option3.setText(currentQuestion.option3)
//    option4.setText(currentQuestion.option4)
//    answer.setText(currentQuestion.correct_answer)
//    q_num += 1
//}




//    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val question: TextView = view.findViewById(R.id.question_name)
//
//        //        val question_description: TextView = view.findViewById(R.id.question_description)
//        val deleteButton: MaterialButton = view.findViewById(R.id.deleteButton)
//    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val view =
//            LayoutInflater.from(parent.context).inflate(R.layout.question_card, parent, false)
//        return MyViewHolder(view)
//    }

//    @SuppressLint("SetTextI18n")
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        holder.question.text = listQuestionsInQuiz.questionsForQuiz. .question.trim()
////        holder.tvTeacher.text = avlSubjectList[position].teacher.trim()
//    }
//
//    override fun getItemCount(): Int {
//        return listQuestionsInQuiz.size
//    }
companion object {
    @SuppressLint("StaticFieldLeak")
     var QUIZ_PASS = "Quiz"
}
}