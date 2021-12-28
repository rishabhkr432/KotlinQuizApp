package com.example.quizkotlin.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.quizkotlin.constants.STUDENT_TABLE

data class User(
    val uid: String,
    val email: String,
    val userType: Int,

) {
    constructor() : this("", "", -1)
}

