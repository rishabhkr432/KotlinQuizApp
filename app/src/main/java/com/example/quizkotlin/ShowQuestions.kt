//package com.example.quizkotlin
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.EditText
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.quizkotlin.models.Question
//import com.example.quizkotlin.models.Quiz
//import com.google.android.material.button.MaterialButton
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.firestore.FirebaseFirestore
//
//class ShowQuestions() : AppCompatActivity() {
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
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.attempt_quiz)
//
//
//        auth = FirebaseAuth.getInstance()
//        database = FirebaseFirestore.getInstance()
//        user = auth.currentUser!!
//        initViews()
//        getQuestions()
//
//    }
//    private fun getQuestions() {
//        questionsList = quizObj.questionsForQuiz
//
//        for (i in questionsList){
//            question =  i.question as EditText
//            option1 =   i.option1 as EditText
//            option2 = i.option2 as EditText
//            option3 = i.option3 as EditText
//            option4 = i.option4 as EditText
//            answer = i.correct_answer as EditText
//        }
//
//
//
//
//    }
//
//
//    private fun initViews() {
//
//            question = findViewById(R.id.show_question)
//            option1 = findViewById(R.id.option1)
//            option2 = findViewById(R.id.option2)
//            option3 = findViewById(R.id.option3)
//            option4 = findViewById(R.id.option4)
//            answer = findViewById(R.id.correct_answer)
//            delete_button = findViewById(R.id.delete_button_demo)
//            next_button = findViewById(R.id.next_button_demo)
//
//            goBack = findViewById(R.id.goBackButton)
//    }
//
//
//
//
////    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
////        val question: TextView = view.findViewById(R.id.question_name)
////
////        //        val question_description: TextView = view.findViewById(R.id.question_description)
////        val deleteButton: MaterialButton = view.findViewById(R.id.deleteButton)
////    }
//
////    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
////        val view =
////            LayoutInflater.from(parent.context).inflate(R.layout.question_card, parent, false)
////        return MyViewHolder(view)
////    }
//
////    @SuppressLint("SetTextI18n")
////    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
////        holder.question.text = listQuestionsInQuiz.questionsForQuiz. .question.trim()
//////        holder.tvTeacher.text = avlSubjectList[position].teacher.trim()
////    }
////
////    override fun getItemCount(): Int {
////        return listQuestionsInQuiz.size
////    }
//}