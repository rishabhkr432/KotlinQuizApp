package com.example.quizkotlin.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizkotlin.R

class CheckResultsAdapter(
    private val optionsList: ArrayList<String?>,
    private var userAnswer: String,
    private var correctAnswer: String,
    private var showAnswer: Boolean,
    private val context: Context,

) : RecyclerView.Adapter<CheckResultsAdapter.MyViewHolder>() {



    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val option: EditText = view.findViewById(R.id.option_name)
        val optionBg: CardView = view.findViewById(R.id.option_card)
        val userAnswer: TextView = view.findViewById(R.id.user_answer)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.options_card, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        var currItem = optionsList[position]

        holder.option.isFocusable = false
        holder.option.isFocusableInTouchMode = false
        holder.option.isClickable = false
        holder.option.setText(optionsList[holder.adapterPosition])
        Log.i(
            "CheckResultsAdapter-",
            "option:${holder.option.text} -  userAnswer:$userAnswer - correctAnswer:$correctAnswer"
        )
        if (holder.option.text.toString() == userAnswer && holder.option.text.toString() == correctAnswer) {
            holder.optionBg.setCardBackgroundColor(holder.itemView.context.getColor(R.color.chartreuse))
            if(showAnswer){ holder.userAnswer.visibility = View.VISIBLE}

        } else if (holder.option.text.toString() == userAnswer && holder.option.text.toString() != correctAnswer) {
            holder.optionBg.setCardBackgroundColor(holder.itemView.context.getColor(R.color.light_red))
            if(showAnswer){ holder.userAnswer.visibility = View.VISIBLE}
        } else if (holder.option.text.toString() != userAnswer && holder.option.text.toString() == correctAnswer) {
            holder.optionBg.setCardBackgroundColor(holder.itemView.context.getColor(R.color.chartreuse))

        }
    }

    override fun getItemCount(): Int {
        return optionsList.size
    }
}