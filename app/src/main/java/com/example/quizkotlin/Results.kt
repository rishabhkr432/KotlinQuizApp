package com.example.quizkotlin

data class Results(
var quizID: String,
var results: Int,
var studentId: String
) { constructor() : this( "", 0,"")}
