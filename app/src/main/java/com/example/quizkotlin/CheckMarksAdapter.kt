package com.example.quizkotlin

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import android.content.Intent
import com.example.quizkotlin.models.Results


class CheckMarksAdapter(
    private val quizBank: ArrayList<Results>,
    private val context: Context,
//
) : RecyclerView.Adapter<CheckMarksAdapter.MyViewHolder>() {

    val database = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance()
    private lateinit var intent: Intent



//    var i = intent!!.

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        if (userType == 1)
//        {}
        val quizcardtitle: TextView = view.findViewById(R.id.quiz_name)
        val marks: TextView = view.findViewById(R.id.quizMarks)


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

        holder.delbtn.visibility = View.GONE;
        holder.viewbtn.visibility = View.GONE
        holder.marks.visibility = View.VISIBLE

//            quizPath = "Student's quiz records"

        holder.quizcardtitle.text = quizBank[position].quizID.trim()
        holder.marks.text = "You scored: " + quizBank[position].results.toString() + "/10"




    }

    override fun getItemCount(): Int {
        return quizBank.size
    }

    companion object {
        private const val TAG = "CheckMarksAdapter"
    }



}

