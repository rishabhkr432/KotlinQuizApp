package com.example.quizkotlin.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.quizkotlin.activities.CheckResultsActivity
import com.example.quizkotlin.R
import com.example.quizkotlin.Constants.STUDENT_QUIZ_RESULTS_PATH
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

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val quizcardtitle: TextView = view.findViewById(R.id.quiz_name)
        val marks: TextView = view.findViewById(R.id.quizMarks)

        val studentDB: MaterialButton = view.findViewById(R.id.quizStudentDB)
        val delbtn: MaterialButton = view.findViewById(R.id.quizDeleteButton)

        val viewbtn: MaterialButton = view.findViewById(R.id.quizViewButton)

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
        holder.studentDB.visibility = View.GONE


        holder.quizcardtitle.text = resultsBank[position].quizId.trim()
        holder.marks.text = "You scored: " + resultsBank[position].results.toString() + "/10"

        holder.viewbtn.setOnClickListener {

            database.collection(quizPath).document(resultsBank[position].quizId)
                .set(resultsBank[position])
                .addOnSuccessListener {
                    sendResult = resultsBank[position]
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

