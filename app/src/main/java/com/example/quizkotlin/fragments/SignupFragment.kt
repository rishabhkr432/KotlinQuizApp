package com.example.quizkotlin.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.quizkotlin.activities.LoginActivity
import com.example.quizkotlin.R
import com.example.quizkotlin.activities.StudentHomeActivity
import com.example.quizkotlin.activities.TeacherHomeActivity
import com.example.quizkotlin.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class SignupFragment : Fragment() {

    private lateinit var btnPrev: Button
    private lateinit var btnSignup: Button
    private lateinit var btnGoogleSignup: Button
    private lateinit var tvLogin: TextView
    private lateinit var tvMsg: TextView
    private lateinit var etEmail: EditText
    private lateinit var etPwd: EditText
    private lateinit var etConfirmPwd: EditText
    private lateinit var progress: ProgressBar

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var database: FirebaseFirestore

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_signup, container, false)
        initViews(view)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
//
        if (container != null) {
            googleSignInClient = GoogleSignIn.getClient(container.context, gso)
        }

        btnPrev.setOnClickListener {
            callUserTypeFragment()
        }

        btnSignup.setOnClickListener {
            validateUserInput()
        }

        btnGoogleSignup.setOnClickListener {
            signIn()
        }

        tvLogin.setOnClickListener {
            val intent = Intent(container?.context, LoginActivity::class.java)
            startActivity(intent)
        }

        return view
    }
    /**
     * Setting views.
     */
    private fun initViews(view: View) {
        btnPrev = view.findViewById(R.id.signup_prev_btn)
        btnSignup = view.findViewById(R.id.signup_signup_btn)
        btnGoogleSignup = view.findViewById(R.id.signup_googleLogin_btn)
        tvLogin = view.findViewById(R.id.signup_login_txt)
        tvMsg = view.findViewById(R.id.signup_msg_tv)
        etEmail = view.findViewById(R.id.signup_email_et)
        etPwd = view.findViewById(R.id.signup_pwd_et)
        etConfirmPwd = view.findViewById(R.id.signup_confirmPwd_et)
        progress = view.findViewById(R.id.signup_progress)
    }

    private fun callUserTypeFragment() {
        val userTypeFragment = UserTypeFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.registration_fragment_container, userTypeFragment)
            .commit()
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun validateUserInput() {
        progress.visibility = View.VISIBLE
        tvMsg.text = "Validating com.example.quizkotlin.UserStarter Input..."
        tvMsg.visibility = View.VISIBLE
        val email = etEmail.text.toString().trim()
        val pwd = etPwd.text.toString().trim()
        val confirmPwd = etConfirmPwd.text.toString().trim()

        if (isEmailValid(email) && isPasswordValid(pwd, confirmPwd)) {
            createAccount(email, pwd)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isEmailValid(email: String): Boolean {
        return if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            true
        } else {
            etEmail.error = "Enter a valid email!"
            etEmail.focusable
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isPasswordValid(pwd: String, confirmPwd: String): Boolean {
        return if (pwd.isNotEmpty() && pwd.length >= 6) {
            if (pwd == confirmPwd) {
                true
            } else {
                etConfirmPwd.error = "Entered text does not matches password"
                false
            }
        } else {
            etPwd.error = "Password length must be at least 6 characters"
            etPwd.focusable
            false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun createAccount(email: String, pwd: String) {
        tvMsg.text = "Creating a new account..."
        activity?.let { it ->
            auth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(it) {
                    if (it.isSuccessful) {
                        Log.d(TAG, "registerUser:success")
                        val user = auth.currentUser
                        if (user != null) {
                            Log.d(TAG, "com.example.quizkotlin.UserStarter: ${user.uid}")
                            storeUserInfo(user)
                        }
                    } else {
                        progress.visibility = View.GONE
                        Log.d(TAG, "registerUser:failure", it.exception)
                        Toast.makeText(activity, "Registration Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener(it) {
                    progress.visibility = View.GONE
                    Toast.makeText(activity, "" + it.message, Toast.LENGTH_SHORT).show()
                }
        }
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
                    Toast.makeText(activity, "Registration Failed!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(activity, "" + it.message, Toast.LENGTH_SHORT).show()
            }
    }

    @SuppressLint("SetTextI18n")
    private fun storeUserInfo(user: FirebaseUser) {
        tvMsg.text = "Storing user info…"
        val newUser = arguments?.let {
            user.email?.let { it1 ->
                User(
                    user.uid,
                    it1, it.getInt("UserType")

                )
            }
        }

        if (newUser != null) {
            database.collection("Users").document(user.uid).set(newUser)
                .addOnSuccessListener {
                    progress.visibility = View.GONE
                    tvMsg.text = "Account Created"
                    if (newUser.userType == 1)
                        startTeacherActivity()
                    else if (newUser.userType == 2)
                        startHomeActivity()
                }
                .addOnFailureListener {
                    progress.visibility = View.GONE
                    tvMsg.text = "Error while storing user info"
                    Log.d(TAG, "Error adding document" + it.message)
                }
        }
    }

    private fun startHomeActivity() {
        val intent = Intent(activity, StudentHomeActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun startTeacherActivity() {
        val intent = Intent(activity, TeacherHomeActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    companion object {
        private const val TAG = "SignupFragment"
        private const val RC_SIGN_IN = 100
    }

}