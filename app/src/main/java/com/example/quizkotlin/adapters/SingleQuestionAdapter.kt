package com.example.quizkotlin.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.quizkotlin.R


class SingleQuestionAdapter(
    private val optionsList: ArrayList<String?>,
    private val context: Context,
    private val listener: MyViewHolder.Listener,
//
) : RecyclerView.Adapter<SingleQuestionAdapter.MyViewHolder>() {
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val option: EditText = view.findViewById(R.id.option_name)

//        if(optionsList.size ==1){
//            option.setEnabled(false)
//            option.setKeyListener(null)
//        }

        interface Listener {
            fun returnPosString(list: ArrayList<String?>)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.options_card, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.option.hint = "Enter Answer"
            val pos = optionsList[holder.adapterPosition]
            holder.option.setText(pos)
        holder.option.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.d(
                    "updatedText",
                    "Position in options:" + pos
                )
                Log.d("Optionslist size", itemCount.toString())
//                listener.returnPosString(optionsList)
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                isTextChanged = true
                if (s.toString().trim() != "") {
                    Log.i("string is", s.toString())
                        optionsList[holder.adapterPosition] = s.toString().trim()
                        listener.returnPosString(optionsList)
                } else {
                    holder.option.error
                    holder.option.requestFocus()

//                    Toast.makeText(holder.itemView.context, "Please enter a value option", Toast.LENGTH_LONG).show()
                }
            }

//            holder.option.visibility = View.GONE
//        listener.optionsReturn(optionsList)



    })
    }


    companion object {
        @SuppressLint("StaticFieldLeak")
        private const val TAG = "AddQuestionAdapter"

    }

    override fun getItemCount(): Int {
        return optionsList.size
    }

}

