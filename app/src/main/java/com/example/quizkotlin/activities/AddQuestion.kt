package com.example.quizkotlin.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.quizkotlin.R
import com.example.quizkotlin.fragments.AddQuestionFragment1
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class AddQuestion : AppCompatActivity() {
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    private val frag1 = AddQuestionFragment1()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_question)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        user = auth.currentUser!!

        supportFragmentManager.beginTransaction()
            .replace(R.id.askQuestion, frag1)
            .commit()

    }
    /**
     * Made this public so it can be called from fragments.
     */
    fun restartFragment(fragmentId: Int) {
        val currentFragment = this.supportFragmentManager.findFragmentById(fragmentId)!!

        this.supportFragmentManager.beginTransaction()
            .detach(currentFragment)
            .commit()
        this.supportFragmentManager.beginTransaction()
            .attach(currentFragment)
            .commit()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private const val TAG = "AddQuestion"
        var QUESTION = "Question"
        var OPTIONS = "Options"
    }
}

