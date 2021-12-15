package com.example.quizkotlin.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.quizkotlin.constants.COLUMN_STUDENT_EMAIL
import com.example.quizkotlin.constants.COLUMN_STUDENT_ID
import com.example.quizkotlin.constants.COLUMN_STUDENT_NAME
import com.example.quizkotlin.constants.COLUMN_STUDENT_PASSWORD
import com.example.quizkotlin.constants.STUDENT_TABLE

data class Student(
    val uid: String,
    val email: String,
    val name: String,
    val password: String
) {
    constructor() : this("", "", "", "")
}

