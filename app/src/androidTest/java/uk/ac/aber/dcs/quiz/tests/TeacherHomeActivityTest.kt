package uk.ac.aber.dcs.quiz

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView

import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import uk.ac.aber.dcs.quiz.activities.TeacherHomeActivity

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class TeacherHomeActivityTest {
    @Test
    fun testActivityNavigation() {

        // SETUP
        val activityScenario = ActivityScenario.launch(TeacherHomeActivity::class.java)
        // Nav DirectorsFragment
        onView(withId(R.id.quiz_bank)).perform(click())
        // VERIFY
        onView(withId(R.id.quiz_bank_title)).check(matches(isDisplayed()))

        Espresso.closeSoftKeyboard()

        onView(withId(R.id.go_back_button_quiz_bank)).perform(click())
////        // VERIFY
        onView(withId(R.id.teacher_home_head)).check(matches(isDisplayed()))
    }
}
