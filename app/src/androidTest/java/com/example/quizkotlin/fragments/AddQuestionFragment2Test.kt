package com.example.quizkotlin.fragments


import androidx.test.espresso.Espresso.onView

import androidx.test.espresso.matcher.ViewMatchers
import com.example.quizkotlin.activities.AddQuestion
import com.example.quizkotlin.R


import org.junit.Before

import org.junit.Test

import android.os.Bundle
import android.view.View


import androidx.fragment.app.testing.launchFragmentInContainer

import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.runner.RunWith
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.typeText

import androidx.test.espresso.matcher.BoundedMatcher

import com.example.quizkotlin.models.Question
import com.example.quizkotlin.models.Quiz
import org.hamcrest.Description
import org.hamcrest.Matcher


@RunWith(AndroidJUnit4::class)
class AddQuestionFragment2Test {
//    @Rule
//    @JvmField
//    val activityTestRule = ActivityTestRule(AddQuestion::class.java)
@Before
fun setUp() {
    val testQuestion = "What is 2+2?"
    val args = Bundle()
    args.putString(AddQuestion.QUESTION,testQuestion )
//        args.putString(AddQuestion.SPINNER, spinnerText)
    args.putInt(AddQuestion.OPTIONS, 1)
    val frag = launchFragmentInContainer<AddQuestionFragment2>(
        fragmentArgs = args,
        themeResId = R.style.Theme_QuizKotlin
    )
}

    @Test
    fun fragment_can_be_instantiated() {

        onView(withId(R.id.add_question)).check(matches(withText("What is 2+2?")))



    }
    @Test
    fun testingInput () {
        val answer = "?"
        val quizList: MutableList<Quiz>  = mutableListOf()
        val questionList: MutableList<Question>  = mutableListOf()

        val options: MutableList<String?> = mutableListOf("7","3","4")
        val question: Question = Question("What is 5+2?",options,"7")
        questionList.add(question)
        val quiz: Quiz = Quiz("English",null,questionList)
        quizList.add(quiz)
        onView(withId(R.id.add_question)).check(matches(withText("What is 2+2?")))
        onView(withId(R.id.options_list_rv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.correct_answer_rv)).perform(typeText(answer))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.save_button)).perform(ViewActions.click())
        onView(withId(R.id.correct_answer_rv)).check(matches(hasErrorText("Please use alphanumeric characters only")))


    }
    fun atPosition(position: Int, itemMatcher: Matcher<View?>): Matcher<View?>? {
        checkNotNull(itemMatcher)
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                    ?: // has no item on such position
                    return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }







//    @Test
//    fun testUI() {
//        Espresso.onView(ViewMatchers.withId(R.id.signIn_login_btn)).perform(ViewActions.click())
//    }
}