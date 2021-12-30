package com.example.quizkotlin

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CheckResultsAdapter(
    private val optionsList: ArrayList<String?>,
    private var userAnswer: String,
    private var correctAnswer: String,


    private val context: Context,

//
) : RecyclerView.Adapter<CheckResultsAdapter.MyViewHolder>() {
    private var tempOp: Int = 0
//    private fun colorChange(v: View ->
//    )

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val option: EditText = view.findViewById(R.id.option_name)
        val optionBg: CardView = view.findViewById(R.id.option_card)



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
//        if ([holder.adapterPosition] == userAnswer) {
//        }

        holder.option.setText(optionsList[holder.adapterPosition])
        Log.i(
            "CheckResultsAdapter-",
            "option:${holder.option.text} -  userAnswer:$userAnswer - correctAnswer:$correctAnswer"
        )
        if (holder.option.text.toString() == userAnswer && holder.option.text.toString() == correctAnswer) {

            holder.optionBg.setCardBackgroundColor(holder.itemView.context.getColor(R.color.chartreuse))
//            holder.option.setBackgroundResource(R.color.chartreuse)

//            ("@color/chartreuse")
        } else if (holder.option.text.toString() == userAnswer && holder.option.text.toString() != correctAnswer) {
            holder.optionBg.setCardBackgroundColor(holder.itemView.context.getColor(R.color.light_red))
        } else if (holder.option.text.toString() != userAnswer && holder.option.text.toString() == correctAnswer) {
            holder.optionBg.setCardBackgroundColor(holder.itemView.context.getColor(R.color.chartreuse))
        }
    }

    override fun getItemCount(): Int {
        return optionsList.size
    }
}