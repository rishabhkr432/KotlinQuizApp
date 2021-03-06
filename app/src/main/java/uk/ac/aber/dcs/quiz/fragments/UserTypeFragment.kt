package com.example.quizkotlin.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.quizkotlin.activities.LoginActivity
import com.example.quizkotlin.R

class UserTypeFragment : Fragment() {

    private lateinit var btnNext: Button
    private lateinit var txtRoles: TextView
    private lateinit var txtLogin: TextView
    private lateinit var rgUser: RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_type, container, false)

        initViews(view)

        btnNext.setOnClickListener {
            verifyUserInput()
        }



        txtLogin.setOnClickListener {
            val intent = Intent(container?.context, LoginActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun initViews(view: View) {
        btnNext = view.findViewById(R.id.userType_next_btn)
        txtLogin = view.findViewById(R.id.userType_login_txt)
        rgUser = view.findViewById(R.id.userType_radioGroup)


    }
    /**
     * this method decides the usertype.
     */
    private fun verifyUserInput() {
        when (rgUser.checkedRadioButtonId) {
            R.id.userType_teacher_rb -> {
                callSignupFragment(1)
            }
            R.id.userType_student_rb -> {
                callSignupFragment(2)
            }
            else -> {
                Toast.makeText(activity, "Select any one user type", Toast.LENGTH_LONG).show()
            }
        }
    }
    /**
     * passinng usertype to the signup fragment.
     */
    private fun callSignupFragment(user: Int) {
        val signupFragment = SignupFragment()
        val args = Bundle()
        args.putInt("UserType", user)
        signupFragment.arguments = args
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.registration_fragment_container, signupFragment).addToBackStack(null)
            .commit()
    }

    companion object {
        private const val TAG = "UserTypeActivity"
    }

}