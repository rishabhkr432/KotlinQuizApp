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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot


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
    private var quizSize: Int = 0



//    var i = intent!!.

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        if (userType == 1)
//        {}
        val quizcardtitle: TextView = view.findViewById(R.id.quiz_name)

            val delbtn: MaterialButton = view.findViewById(R.id.quizDeleteButton)

        val viewbtn: MaterialButton = view.findViewById(R.id.quizViewButton)
        val studentDB: MaterialButton = view.findViewById(R.id.quizStudentDB)

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
            holder.delbtn.visibility = View.GONE;
            holder.studentDB.visibility = View.GONE;
            holder.viewbtn.text = "Start"
        }
        holder.quizcardtitle.text = "Quiz title - ${quizBank[position].id.trim()}"


        holder.delbtn.setOnClickListener {
            database.collection(quizPath).whereEqualTo("id",quizBank[position].id.trim())
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        document.reference.delete()
                        Toast.makeText(context, "Quiz deleted", Toast.LENGTH_SHORT).show()
                        quizBank.removeAt(position)
                        Log.d(TAG, "Deleting - ${document.id} => ${document.data}")
                        Log.d(TAG, "Deleting - $position from $quizBank")

                    }

                    notifyItemRemoved(holder.adapterPosition)
                    notifyItemRangeChanged(holder.adapterPosition, quizBank.size)
                    notifyDataSetChanged();

                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                    Log.w(TAG, "Error getting documents: ", exception)
                }
            Log.d(TAG, quizBank[position].id.trim())
            }
//        }
        holder.viewbtn.setOnClickListener {
            quizSize = quizBank[position].questionsForQuiz.size
            Log.d(TAG, "QuizSize - $quizSize")
            if (quizSize in 1..10) {
                database.collection(quizPath).document(quizBank[position].id.trim())
                    .set(quizBank[position])
                    .addOnSuccessListener {
                        sendQuiz = quizBank[position]
//                        showquestion = ShowQuestions(quizBank[position])

                        intent = Intent(holder.itemView.context, AttemptQuizActivity::class.java)
                        intent.putExtra(AttemptQuizActivity.QUIZ_PASS, sendQuiz)
                        intent.putExtra(AttemptQuizActivity.USER_PASS, userType)


                        Toast.makeText(holder.itemView.context, "Opening Quiz", Toast.LENGTH_SHORT)
                            .show()
                        holder.itemView.context.startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            holder.itemView.context,
                            "Failed to open Quiz",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

            } else {
                Toast.makeText(holder.itemView.context, "Quiz size is out of bounds. Try adding questions.", Toast.LENGTH_SHORT)
                    .show()
                Log.d("${TAG}-", "Quiz size is out of bounds:  Current size: ${quizSize}/10")

            }
        }
        holder.studentDB.setOnClickListener {
            quizSize = quizBank[position].questionsForQuiz.size
            Log.d(TAG, "QuizSize - $quizSize")
            if (quizSize == 10) {
                database.collection("Quizzes")
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            if (document.get("id") == (holder.quizcardtitle.text.toString())) {
                                Log.d(
                                    "holder.quizcardtitle.text",
                                    holder.quizcardtitle.text.toString()
                                )

                                val docRef =
                                    database.collection("Student's quizzes").document(document.id)
                                docRef.get()
                                    .addOnCompleteListener(OnCompleteListener<DocumentSnapshot?> { task ->
                                        if (task.isSuccessful) {
                                            val doc = task.result
                                            if (doc.exists()) {

                                                Toast.makeText(
                                                    holder.itemView.context,
                                                    "Failed to send Quiz, document already exists",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                Log.d(
                                                    "${TAG}-",
                                                    "sendQuiztoStudentDatabase:  Document exists: ${document.id} ${holder.quizcardtitle.text}"
                                                )
                                            } else {
                                                Log.d(
                                                    "${TAG}-",
                                                    "Document ${document.id} does not exist!"
                                                )
                                                val save = (document.toObject(Quiz::class.java))
                                                docRef.set(save)
                                                    //                    docRef.set((quid.id.toObject(Quiz::class.java))
                                                    .addOnSuccessListener {
                                                        Log.d(
                                                            "${TAG}-",
                                                            "sendQuiztoStudentDatabase- Sending quiz to the student database -  ${document.id} ${holder.quizcardtitle.text}"
                                                        )
                                                        Toast.makeText(
                                                            holder.itemView.context,
                                                            "Quiz send to the student database",
                                                            Toast.LENGTH_LONG
                                                        ).show()

                                                        //                            makeInputFieldEmpty()
                                                    }
                                                    .addOnFailureListener {
                                                        Toast.makeText(
                                                            holder.itemView.context,
                                                            "Failed to send quiz",
                                                            Toast.LENGTH_LONG
                                                        )
                                                            .show()
                                                        //                            makeInputFieldEmpty()
                                                        Log.d(
                                                            "${TAG}- sendQuiztoStudentDatabase: ",
                                                            "Failed to send quiz"
                                                        )
                                                    }
                                            }


                                        } else {
                                            Log.d("${TAG}-", "Failed with:  ${task.exception}")
                                        }
                                    })
                            }

//                .addOnSuccessListener {
//                    Toast.makeText(holder.itemView.context, "Quiz send to student database", Toast.LENGTH_LONG).show()

//                }
//                .addOnFailureListener {
//                    Toast.makeText(holder.itemView.context, "Failed to send Quiz", Toast.LENGTH_LONG).show()
//
                        }
                    }
            } else {
                Toast.makeText(holder.itemView.context, "Quiz size is too small:  Current size: ${quizSize}/10", Toast.LENGTH_LONG).show()
                Log.d("${TAG}-", "Quiz size is too small:  Current size${quizSize}/10")
            }
        }

    }

    override fun getItemCount(): Int {
        return quizBank.size
    }
    companion object {
        private const val TAG = "ModifyQuizBank"
    }



}



fun <E> ArrayList<E>.add(element: String) {

}
