//package com.example.quizkotlin.viewmodels
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import com.example.quizkotlin.models.Teacher
//import com.example.quizkotlin.repositories.TeacherRepository
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//
//class TeacherViewModel(application: Application) : AndroidViewModel(application) {
//    private val teacherRepository : TeacherRepository = TeacherRepository(application)
//
//    fun insertTeacher(teacher: Teacher){
//            teacherRepository.insertTeacher(teacher)
//    }
//
//    fun checkTeacher(email:String) : Boolean{
//        var isPresent : Boolean = false
//            isPresent = teacherRepository.checkTeacher(email)
//        return isPresent
//    }
//
//    fun checkTeacher(email: String, password : String) : Boolean{
//        var isPresent : Boolean = false
//            isPresent = teacherRepository.checkTeacher(email, password)
//        return isPresent
//    }
//}