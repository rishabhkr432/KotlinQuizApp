package com.example.quizkotlin

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizkotlin.models.Question
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ShowQuestions(
    private val listQuestionBank: ArrayList<Question>,
    private val context: Context
) : RecyclerView.Adapter<ShowQuestions.MyViewHolder>() {
    val database = FirebaseFirestore.getInstance()


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val question: TextView = view.findViewById(R.id.question_name)

        //        val question_description: TextView = view.findViewById(R.id.question_description)
        val deleteButton: MaterialButton = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.question_card, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.question.text = listQuestionBank[position].question.trim()
//        holder.tvTeacher.text = avlSubjectList[position].teacher.trim()
    }

    override fun getItemCount(): Int {
        return listQuestionBank.size
    }
}