package com.example.quizkotlin.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quizkotlin.R
import com.example.quizkotlin.fragments.UserTypeFragment
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val userTypeFragment = UserTypeFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.registration_fragment_container, userTypeFragment)
            .commit()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, StudentHomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")

        private const val TAG = "RegistrationActivity"
    }

}