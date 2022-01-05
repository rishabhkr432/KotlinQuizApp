package com.example.quizkotlin

import com.example.quizkotlin.models.Quiz
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun getQuiz(){
        val q = "TestQuiz"
        val addQuiz = AddQuiz()
        val check = addQuiz.validation(q)
        assertEquals(4, 2 + 2)
    }
}