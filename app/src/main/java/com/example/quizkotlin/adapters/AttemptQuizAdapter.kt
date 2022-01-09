package com.example.quizkotlin.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.quizkotlin.R


class AttemptQuizAdapter(
    private val optionsList: ArrayList<String?>,

    private val context: Context,
    private val listener: MyViewHolder.Listener,
//
) : RecyclerView.Adapter<AttemptQuizAdapter.MyViewHolder>() {
    private var mCheckedPostion = -1


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val option: RadioButton = view.findViewById(R.id.option_name_radio)
        /**
         * Initialised to send data back to the activity.
         * This sends back options text.
         */

        interface Listener {
            fun returnPosString(answer: String)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.options_card_radio, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.option.text = optionsList[holder.adapterPosition]
        holder.option.isChecked = holder.adapterPosition == mCheckedPostion
        holder.option.setOnClickListener {
            if (holder.adapterPosition == mCheckedPostion) {
                holder.option.isChecked = false
                mCheckedPostion = -1
            } else {
                mCheckedPostion = holder.adapterPosition

                optionsList[mCheckedPostion]?.let { it1 -> listener.returnPosString(it1.lowercase()) }
                notifyDataSetChanged()
            }
        }
    }


    companion object {
        @SuppressLint("StaticFieldLeak")
        private const val TAG = "AttemptQuizAdapter"

    }

    override fun getItemCount(): Int {
        return optionsList.size
    }

}

