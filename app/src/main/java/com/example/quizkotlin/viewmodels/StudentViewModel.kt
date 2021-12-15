//package com.example.quizkotlin.viewmodels
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import com.example.quizkotlin.models.Student
//import com.example.quizkotlin.repositories.StudentRepository
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//
//class StudentViewModel(application: Application) : AndroidViewModel(application) {
//    private val studentRepository : StudentRepository = StudentRepository(application)
//    fun insertStudent(student: Student){
//        studentRepository.insertStudent(student)
//    }
//
//    fun checkStudent(email : String) : Boolean{
//        var isPresent : Boolean = false
//            isPresent = studentRepository.checkStudent(email)
//        return isPresent
//    }
//
//    fun checkStudent(email: String, password:String) : Boolean{
//        var isPresent : Boolean = false
//            isPresent = studentRepository.checkStudent(email, password)
//        return isPresent
//    }
//}