package com.example.quizkotlin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.quizkotlin.fragments.QuestionViewModel
import com.example.quizkotlin.models.Question
import com.example.quizkotlin.models.Quiz
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestRule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @get:Rule
    var rule: TestRule =  InstantTaskExecutorRule()
    lateinit var qvm:QuestionViewModel

    /**
     * Testing if the quiz can be create successfully and only outputs quizId instead of all the feilds
     */

    @Test
    fun testQuizNameOutput() {
        val studentId: MutableList<String?> = mutableListOf("student@gmail.com","student1@gmail.com")
        val quiz: Quiz = Quiz("Maths",studentId)
        assertEquals("Maths", quiz.toString());
    }

    @Test
    fun testingMockQuiz(){
        val quizList: MutableList<Quiz>  = mutableListOf()
        val questionList: ArrayList<Question>  = arrayListOf()

        val options: MutableList<String?> = mutableListOf("7","3","4")
        val question: Question = Question("What is 5+2?",options,"7")
        questionList.add(question)
        val quiz: Quiz = Quiz("English",null,questionList)
        quizList.add(quiz)
        assertEquals(quizList[0].quizQuestionList[0].question,"What is 5+2?")
        assertEquals(quizList[0].quizQuestionList[0].correct_answer,"7")

    }

}
