package com.example.quizkotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.quizkotlin.models.Question
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.quizkotlin.models.Quiz
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.DocumentSnapshot

import com.google.android.gms.tasks.OnCompleteListener





class AddQuestionAdapter(
    private val optionsList: ArrayList<String?>,

    private val context: Context,
    private val listener: MyViewHolder.Listener,
//
) : RecyclerView.Adapter<AddQuestionAdapter.MyViewHolder>() {
private val optionsTotal:  ArrayList<String> = arrayListOf()
    private var isTextChanged = false

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val option: EditText = view.findViewById(R.id.option_name)
        public interface Listener {
            fun optionsReturn(list : ArrayList<String?>);
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.options_card, parent, false)
        return MyViewHolder(view)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        var currItem = optionsList[position]
        holder.option.setText(optionsList[holder.adapterPosition])
//        optionsTotal.add(optionData.text.toString())
//        Log.i("optionStr", optionsTotal.toString())
        holder.option.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                Log.d("updatedText", "Position in options:" + optionsList[holder.adapterPosition])
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                isTextChanged = true
                if (s.toString() .trim() != "") {
                    Log.i("s", s.toString())
                    optionsList[holder.adapterPosition] = s.toString().trim()
                    listener.optionsReturn(optionsList)
                }
                else{
                    Log.i("works", s.toString())
                }
            }



        }
                )
//        listener.optionsReturn(optionsList)




    }




//    companion object {
//        @SuppressLint("StaticFieldLeak")
//        private const val TAG = "AddQuestion"
//        lateinit var addquestionActivity: Activity
//        var QUESTION = "Question"
//        var SPINNER = "Spinner"
//        var OPTIONS = "Options"
//    }
override fun getItemCount(): Int {
    return optionsList.size
}

}

