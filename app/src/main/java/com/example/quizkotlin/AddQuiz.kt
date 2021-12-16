package com.example.quizkotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.quizkotlin.fragments.UserTypeFragment
import com.google.firebase.auth.FirebaseAuth

class AddQuiz : AppCompatActivity(){
    private var addquiz : Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loginIntent = Intent(this, QuestionBank::class.java)
        loginIntent.putExtra("addquizcheck",addquiz)
        Log.d(TAG, "send value:" + addquiz)
        startActivity(loginIntent)
        Log.d(TAG, "Entered addQuiz activity")




    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        private const val TAG = "AddQuiz"
    }
}