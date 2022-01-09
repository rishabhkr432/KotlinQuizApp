package com.example.quizkotlin.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.quizkotlin.R
import com.example.quizkotlin.models.User

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

class LoginActivity : AppCompatActivity() {

    private lateinit var btnLogin: Button
    private lateinit var btnGoogleLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var tvForgotPwd: TextView
    private lateinit var etEmail: EditText
    private lateinit var etPwd: EditText
    private lateinit var tvWarning: TextView
    private lateinit var tvMsg: TextView
    private lateinit var progress: ProgressBar
    private lateinit var layout: ScrollView
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var database: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        googleSignInClient = GoogleSignIn.getClient(this, gso)

        btnLogin.setOnClickListener {
            if (etEmail.text.toString().trim().isNotEmpty() &&
                etPwd.text.toString().trim().isNotEmpty()
            ) {
                Log.d(TAG, etEmail.text.toString().trim())
                Log.d(TAG, etPwd.text.toString().trim())
                login()
            } else
                Toast.makeText(this, "Email and Password can't be empty!", Toast.LENGTH_SHORT)
                    .show()
        }

        tvForgotPwd.setOnClickListener {
            showPasswordRecoverDialog()
        }

        btnGoogleLogin.setOnClickListener {
            signIn()
        }

        tvRegister.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initViews() {
        btnLogin = findViewById(R.id.signIn_login_btn)
        btnGoogleLogin = findViewById(R.id.signin_googleLogin_btn)
        tvRegister = findViewById(R.id.signIn_register_txt)
        tvForgotPwd = findViewById(R.id.signIn_forgotPwd_txt)
        etEmail = findViewById(R.id.signIn_email_et)
        etPwd = findViewById(R.id.signIn_pwd_et)
        tvWarning = findViewById(R.id.signIn_warning_tv)
        tvMsg = findViewById(R.id.signin_msg_tv)
        progress = findViewById(R.id.login_progress)
        layout = findViewById(R.id.login_rootLayout)

    }

    @SuppressLint("SetTextI18n")
    private fun login() {
        progress.visibility = View.VISIBLE
        tvMsg.visibility = View.VISIBLE

        val email = etEmail.text.toString().trim()
        val pwd = etPwd.text.toString().trim()

        auth.signInWithEmailAndPassword(email, pwd)
            .addOnSuccessListener {
                progress.visibility = View.GONE
                tvWarning.visibility = View.INVISIBLE
                tvMsg.text = "Successful"
                Log.d(TAG, "signInWithEmail:success")

                val user = it.user
                if (user != null) {
                    callUserSpecificActivity(user)
                }
            }
            .addOnFailureListener(this) {
                tvWarning.visibility = View.VISIBLE
                progress.visibility = View.GONE
                tvMsg.visibility = View.GONE
                Toast.makeText(this, "" + it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun startStudentActivity() {
        val intent = Intent(this, StudentHomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startTeacherActivity() {
        val intent = Intent(this, TeacherHomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Log.w(TAG, "Google sign in failed", e)
                }
            } else {
                Log.w(TAG, task.exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "registerUser:success")
                    val user = auth.currentUser
                    if (user != null) {
                        Log.d(TAG, "com.example.quizkotlin.UserStarter: ${user.uid}")
                        storeUserInfo(user)
                    }
                } else {
                    Log.d(TAG, "registerUser:failure", it.exception)
                    Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "" + it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeUserInfo(user: FirebaseUser) {
        val newUser = user.email?.let { User(user.uid, it, 2) }

        if (newUser != null) {
            database.collection("Users").document(user.uid).set(newUser)
                .addOnSuccessListener {
                    Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT)
                        .show()
                    if (newUser.userType == 1)
                        startTeacherActivity()
                    else if (newUser.userType == 2)
                        startStudentActivity()
                }
                .addOnFailureListener {
                    Log.d(TAG, "Error adding document" + it.message)
                }
        }
    }

    /**
     * Password recovery
     */

    private fun showPasswordRecoverDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Recover Password")

        //set layout
        val linearLayout = LinearLayout(this)

        //views to be set in the dialog
        val emailEt = EditText(this)
        emailEt.hint = "Email"
        emailEt.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        emailEt.minEms = 16
        linearLayout.addView(emailEt)
        linearLayout.setPadding(10, 10, 10, 10)
        builder.setView(linearLayout)

        // recover button
        builder.setPositiveButton(
            "Recover"
        ) { _, i ->
            val inputEmail = emailEt.text.toString().trim { it <= ' ' }
            if (inputEmail != "") {
                beginRecovery(inputEmail)
            } else {
                Snackbar.make(layout, "Please enter email..", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getColor(R.color.color_primary))
                    .show()
            }
        }

        // cancel button
        builder.setNegativeButton(
            "Cancel"
        ) { dialogInterface, _ -> dialogInterface.dismiss() }
        builder.create().show()
    }

    private fun beginRecovery(email: String) {
        progress.visibility = View.VISIBLE
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                progress.visibility = View.GONE
                if (it.isSuccessful)
                    Snackbar.make(layout, "Email Sent", Snackbar.LENGTH_LONG)
                        .setBackgroundTint(getColor(R.color.color_primary))
                        .show()
                else
                    Snackbar.make(layout, "Failed!", Snackbar.LENGTH_LONG)
                        .setBackgroundTint(getColor(R.color.color_primary))
                        .show()
            }
            .addOnFailureListener {
                progress.visibility = View.GONE
                Snackbar.make(layout, "Failed!", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getColor(R.color.color_primary))
                    .show()
            }
    }

    @SuppressLint("SetTextI18n")
    private fun callUserSpecificActivity(user: FirebaseUser) {
        tvMsg.text = "Processing Your data..."
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
                        val currentUser = doc.toObject(User::class.java)
                        if (currentUser != null) {
                            if (currentUser.userType == 1) {
                                startTeacherActivity()
                                Log.d("$TAG", "Loading Teacher's activity")
                            } else if (currentUser.userType == 2) {
                                startStudentActivity()
                                Log.d("$TAG", "Loading Student activity")
                            }
                        }
                    }
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            callUserSpecificActivity(currentUser)
        }
    }


    companion object {
        private const val TAG = "LoginActivity"
        private const val RC_SIGN_IN = 101
    }
}