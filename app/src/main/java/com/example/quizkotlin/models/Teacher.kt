package com.example.quizkotlin.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.quizkotlin.constants.COLUMN_TEACHER_EMAIL
import com.example.quizkotlin.constants.COLUMN_TEACHER_ID
import com.example.quizkotlin.constants.COLUMN_TEACHER_NAME
import com.example.quizkotlin.constants.COLUMN_TEACHER_PASSWORD
import com.example.quizkotlin.constants.TEACHER_TABLE

data class Teacher(
    val uid: String,
    val email: String,
    val name: String,
    val password: String
) {
    constructor() : this("", "", "", "")
}