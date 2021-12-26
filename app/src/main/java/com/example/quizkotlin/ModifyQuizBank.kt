package com.example.quizkotlin

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.quizkotlin.models.Quiz
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.ktx.userProfileChangeRequest


class ModifyQuestion(
    private val quizBank: ArrayList<Quiz>,
    private val userType: Int,
    private var quizPath: String,
//    private val questionsForQuiz:  ArrayList<Question>,
    private val context: Context,
//
) : RecyclerView.Adapter<ModifyQuestion.MyViewHolder>() {
    private val view_btn: String = "Add question"
    private val delete_btn: String = "Delete"
//    private lateinit var showquestion: ShowQuestions
    private lateinit var sendQuiz: Quiz
    val database = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance()
    private lateinit var intent: Intent


//    var i = intent!!.

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        if (userType == 1)
//        {}
        val quizcardtitle: TextView = view.findViewById(R.id.quiz_name)

            val delbtn: MaterialButton = view.findViewById(R.id.quizDeleteButton)

        val viewbtn: MaterialButton = view.findViewById(R.id.quizViewButton)

//        init {
//            initClickListeners()
//        }
//
////And pass data here with invoke
//
//
//        private fun initClickListeners() {
//            itemView.setOnClickListener { clickListener.invoke() }
//        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.quiz_card, parent, false)
        return MyViewHolder(view)
    }



    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val docRef = database.collection("Quizzes").document()
        if (userType ==2){
            holder.delbtn.visibility = View.INVISIBLE;
            holder.viewbtn.text = "Start"
//            quizPath = "Student's quiz records"
        }
        holder.quizcardtitle.text = quizBank[position].id.trim()



//        if (questionbanklist.contains(questionbanklist[position].id.trim())) {
//
//        }
        holder.delbtn.setOnClickListener {
            database.collection(quizPath).whereEqualTo("id",quizBank[position].id.trim())
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        document.reference.delete()
                        Toast.makeText(context, "Quiz deleted", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Deleting - ${document.id} => ${document.data}")



                    }

                    notifyItemRemoved(holder.adapterPosition)
                    notifyItemRangeChanged(holder.adapterPosition, quizBank.size)


//

                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                    Log.w(TAG, "Error getting documents: ", exception)
                }
            Log.d(TAG, quizBank[position].id.trim())
            }
//        }
        holder.viewbtn.setOnClickListener {
            database.collection(quizPath).document(quizBank[position].id.trim())
                .set(quizBank[position])
                .addOnSuccessListener {
                    sendQuiz = quizBank[position]
//                        showquestion = ShowQuestions(quizBank[position])
                    if (userType == 2) {
                        intent = Intent(holder.itemView.context, AttemptQuizActivity::class.java)
                        intent.putExtra(AttemptQuizActivity.QUIZ_PASS, sendQuiz)
                    }
                    else {

                        intent = Intent(holder.itemView.context, ShowQuestions::class.java)
                        intent.putExtra(
                            ShowQuestions.QUIZ_PASS,
                            sendQuiz
                        ) //Put your id to your next Intent
                    }
                    Toast.makeText(holder.itemView.context, "Opening Quiz", Toast.LENGTH_SHORT).show()
                   holder.itemView.context.startActivity(intent)
                }
                .addOnFailureListener {
//                    holder.deletebtn_card.text = "Un Enroll"
                    Toast.makeText(holder.itemView.context, "Failed to open Quiz", Toast.LENGTH_SHORT).show()
                }


//            else if (holder.viewbtn.text == view_btn) {
//                database.collection("Quizzes").document(quizBank[position].id.trim())
//                    .set(quizBank[position])
//                    .addOnSuccessListener {
//                        Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show()
//                        Quizz.questionsForQuiz.add(quizBank[position].questionID.trim())
//                        Log.d(TAG, questionsForQuiz.toString())
//                    }

        }

    }

    override fun getItemCount(): Int {
        return quizBank.size
    }
//    fun onClick(v: View?) {
//        notifyItemRemoved(this.getLayoutPosition())
//    }
    companion object {
        private const val TAG = "ModifyQuizBank"
    }



}


fun <E> ArrayList<E>.add(element: String) {

}
