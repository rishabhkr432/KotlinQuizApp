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
import com.example.quizkotlin.Quizz.questionsForQuiz
import com.example.quizkotlin.models.Question
import com.example.quizkotlin.models.Quiz
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ModifyQuestion(
    private val questionBank: ArrayList<Question>,
    private val checkaddquiz: Boolean,
    private val questionsForQuiz:  ArrayList<Question>,
    private val context: Context,
    private val add_btn: String = "Add question",
    private val delete_btn: String = "Delete"
) : RecyclerView.Adapter<ModifyQuestion.MyViewHolder>() {

    val database = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance()


//    var i = intent!!.

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val questioncardtitle: TextView = view.findViewById(R.id.question_name)
        val delbtn: MaterialButton = view.findViewById(R.id.deleteButton)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.question_card, parent, false)
        return MyViewHolder(view)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val docRef = database.collection("Quizzes").document()

        holder.questioncardtitle.text = questionBank[position].question.trim()
//        Log.d(TAG, questionBank.toString())
        Log.d(TAG, "deleted question: "+ holder.questioncardtitle.text.toString())
        if (checkaddquiz){
            holder.delbtn.text = "Add question"
        }


//        if (questionbanklist.contains(questionbanklist[position].id.trim())) {
//
//        }
        holder.delbtn.setOnClickListener {
            if (holder.delbtn.text == delete_btn) {
                database.collection("Questions").document(questionBank[position].questionID.trim())
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(context, "Question deleted", Toast.LENGTH_SHORT).show()

                    }
                    .addOnFailureListener {
//                    holder.deletebtn_card.text = "Un Enroll"
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                    }
            }
            else if (holder.delbtn.text == add_btn) {
                database.collection("Questions").document(questionBank[position].questionID.trim())
                    .set(questionBank[position])
                    .addOnSuccessListener {
                        Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show()
                        Quizz.questionsForQuiz.add(questionBank[position].questionID.trim())
                        Log.d(TAG, questionsForQuiz.toString())
                    }
            }
        }

    }
    override fun getItemCount(): Int {
        return questionBank.size
    }
    companion object {
        private const val TAG = "ModifyQuestion"
    }



}

fun <E> ArrayList<E>.add(element: String) {

}
