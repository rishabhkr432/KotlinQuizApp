package com.example.quizkotlin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import com.example.quizkotlin.constants.SCORE
import com.example.quizkotlin.databinding.ActivityStudentHomeBinding

class StudentHomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStudentHomeBinding
    private var myscore : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_home)
        binding.logOut.setOnClickListener {
            val builder = AlertDialog.Builder(this@StudentHomeActivity)
            val viewGroup = findViewById<ViewGroup>(android.R.id.content)
            val dialogView = LayoutInflater.from(it.getContext())
                .inflate(
                    R.layout.custom_dialog_layout_3,
                    viewGroup,
                    false
                )
            builder.setView(dialogView)
            val dialog = builder.create()
            dialog.show()
            val log_out: AppCompatButton = dialogView.findViewById(R.id.log_out)
            log_out.setOnClickListener {
                dialog.dismiss()
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        }

//        binding.attemptQuiz.setOnClickListener {
//            startActivity(Intent(this, AttemptQuizActivity::class.java))
//        }

        binding.marksOfQuiz.setOnClickListener {
            val builder = AlertDialog.Builder(this@StudentHomeActivity)
            val viewGroup = findViewById<ViewGroup>(android.R.id.content)
            val dialogView = LayoutInflater.from(it.getContext())
                .inflate(
                    R.layout.custom_dialog_layout_4,
                    viewGroup,
                    false
                )
            builder.setView(dialogView)
            val dialog = builder.create()
            dialog.show()
            myscore = java.lang.String.valueOf(SCORE)
            val score = dialogView.findViewById<TextView>(R.id.score_tv)
            score.text = myscore
            val continuebtn: AppCompatButton = dialogView.findViewById(R.id.score_continue)
            continuebtn.setOnClickListener { dialog.dismiss() }
        }
    }
}