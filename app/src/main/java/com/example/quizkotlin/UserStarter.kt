package com.example.quizkotlin

import android.annotation.SuppressLint
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizkotlin.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

open class UserStarter  : AppCompatActivity() {
    private lateinit var tvDisplayName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvUserType: TextView
    private lateinit var tvLogout: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

//        initViews()
    private fun getUser(){
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        val user = auth.currentUser

        if (user != null) {
            database.collection("Users").document(user.uid).
                /**
                 * Reads the document referenced by this `DocumentReference`.
                 *
                 * @return A Task that will be resolved with the contents of the Document at this `DocumentReference`.
                 */
            get(Source.DEFAULT)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val doc = it.result
                        if (doc.exists()) {
                            val userDetail = doc.toObject(User::class.java)
                            if (userDetail != null) {
                                populateUserDetail(userDetail)
                            }
                        } else {
                            Toast.makeText(
                                this,
                                "Error while fetching your data from server: ${it.exception}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

        }
    }





    @SuppressLint("SetTextI18n")
    private fun populateUserDetail(user: User) {
        tvDisplayName.text = user.email[0].uppercaseChar().toString()
        tvEmail.text = user.email
        when (user.userType) {
            1 -> tvUserType.text = "Teacher"
            2 -> tvUserType.text = "Student"
            else -> tvUserType.text = "com.example.quizkotlin.UserStarter Type Not determined"
        }

    }

}