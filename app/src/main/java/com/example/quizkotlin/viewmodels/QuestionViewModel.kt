//package com.example.quizkotlin.viewmodels
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import com.example.quizkotlin.models.Question
//import com.example.quizkotlin.repositories.QuestionRepository
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//
//class QuestionViewModel(application: Application) : AndroidViewModel(application) {
//    private val questionRepository : QuestionRepository = QuestionRepository(application)
//    private val allQuestionList : LiveData<List<Question>> = questionRepository.getAllQuestions()
//
//    fun insertQuestion(question: Question){
//        questionRepository.insertQuestions(question)
//    }
//
//    fun getAllQuestionList() : LiveData<List<Question>>{
//        return allQuestionList
//    }
//}