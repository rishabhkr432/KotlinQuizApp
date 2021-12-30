package com.example.quizkotlin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.quizkotlin.constants.STUDENT_QUIZ_RESULTS_PATH
import com.example.quizkotlin.models.Results
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class HistoryAdapter(
    private val resultsBank: ArrayList<Results>,
    private val context: Context,
//
) : RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    val database = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance()
    private lateinit var intent: Intent
    private lateinit var sendResult: Results
    private var quizPath = STUDENT_QUIZ_RESULTS_PATH


//    var i = intent!!.

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //        if (userType == 1)
//        {}
        val quizcardtitle: TextView = view.findViewById(R.id.quiz_name)
        val marks: TextView = view.findViewById(R.id.quizMarks)

        val studentDB: MaterialButton = view.findViewById(R.id.quizStudentDB)
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

        holder.delbtn.visibility = View.GONE
        holder.viewbtn.visibility = View.VISIBLE
//        holder.marks.visibility = View.VISIBLE
        holder.studentDB.visibility = View.GONE

//            quizPath = "Student's quiz records"

        holder.quizcardtitle.text = resultsBank[position].quizID.trim()
        holder.marks.text = "You scored: " + resultsBank[position].results.toString() + "/10"

        holder.viewbtn.setOnClickListener {

            database.collection(quizPath).document(resultsBank[position].quizID)
                .set(resultsBank[position])
                .addOnSuccessListener {
                    sendResult = resultsBank[position]
//                        showquestion = ShowQuestions(quizBank[position])

                    intent = Intent(holder.itemView.context, CheckResultsActivity::class.java)
                    intent.putExtra(CheckResultsActivity.RESULT_PASS, sendResult)


                    Toast.makeText(holder.itemView.context, "Opening Quiz", Toast.LENGTH_SHORT)
                        .show()
                    holder.itemView.context.startActivity(intent)
                }
        }


    }

    override fun getItemCount(): Int {
        return resultsBank.size
    }

    companion object {
        private const val TAG = "HistoryAdapter"
    }


}

