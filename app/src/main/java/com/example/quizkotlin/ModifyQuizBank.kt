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
import androidx.core.content.ContextCompat.startActivity

import android.os.Bundle

import android.content.Intent
import androidx.core.content.ContextCompat


class ModifyQuestion(
    private val quizBank: ArrayList<Quiz>,
//    private val checkaddquiz: Boolean,
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
    var quizPos: Int = 0


//    var i = intent!!.

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
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



    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val docRef = database.collection("Quizzes").document()

        holder.quizcardtitle.text = quizBank[position].id.trim()
//        Log.d(TAG, questionBank.toString())
        Log.d(TAG, "deleted question: "+ holder.quizcardtitle.text.toString())



//        if (questionbanklist.contains(questionbanklist[position].id.trim())) {
//
//        }
        holder.delbtn.setOnClickListener {
            database.collection("Quizzes").whereEqualTo("id",quizBank[position].id.trim())
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
            database.collection("Quizzes").document(quizBank[position].id.trim())
                .set(quizBank[position])
                .addOnSuccessListener {
                    sendQuiz = quizBank[position]
//                        showquestion = ShowQuestions(quizBank[position])
//                        val intent = Intent(this, ShowQuestions::class.java)
//                         //Your id
//
//                        intent.putExtras("quizObj", quizBank[position]) //Put your id to your next Intent
//
//                        startActivity(intent)
//                        finish()
                    Toast.makeText(context, "Opening Quiz", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
//                    holder.deletebtn_card.text = "Un Enroll"
                    Toast.makeText(context, "Failed to open Quiz", Toast.LENGTH_SHORT).show()
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
