package com.example.quizkotlin

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.quizkotlin.activities.LoginActivity

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class LoginActivityTest {
    @Before
    fun setUp() {
    }
    // this only works when user is logged out
    @Test
    fun testLoginUI() {
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        val email = "teacher@gmail.com"
        val password = "123456"
        onView(withId(R.id.signIn_email_et)).perform(typeText(email))
        onView(withId(R.id.signIn_pwd_et)).perform(typeText(password))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.signIn_login_btn)).perform(ViewActions.click())

        onView(withId(R.id.quiz_bank)).check(matches(isDisplayed()))
    }
}