package com.example.quizkotlin

object constants {
    // Student table name
    const val STUDENT_TABLE = "student"

    // Student table name
    const val TEACHER_TABLE = "teacher"

    //Questions table name
    const val QUESTIONS_TABLE = "questions"

    //Student Table Columns Names
    const val COLUMN_STUDENT_ID = "student_id"
    const val COLUMN_STUDENT_NAME = "student_name"
    const val COLUMN_STUDENT_EMAIL = "student_email"
    const val COLUMN_STUDENT_PASSWORD = "student_password"

    //MAX questions
    const val TOTAL_QUESTIONS_TO_ADD = "total_question_to_add"

    //Teacher Table Columns Names
    const val COLUMN_TEACHER_ID = "teacher_id"
    const val COLUMN_TEACHER_NAME = "teacher_name"
    const val COLUMN_TEACHER_EMAIL = "teacher_email"
    const val COLUMN_TEACHER_PASSWORD = "teacher_password"

    //Questions Table Columns Names
    const val COLUMN_QUESTION_ID = "question_id"
    const val COLUMN_QUESTION_SET = "question_set"
    const val COLUMN_SUBJECT_NAME = "subject_name"
    const val COLUMN_QUESTION = "question"
    const val COLUMN_OPTION_A = "option_A"
    const val COLUMN_OPTION_B = "option_B"
    const val COLUMN_OPTION_C = "option_C"
    const val COLUMN_OPTION_D = "option_D"
    const val COLUMN_ANSWER = "answer"

    //Questions Set
    const val QUESTION_SET_A = "Set A"
    const val QUESTION_SET_B = "Set B"
    const val QUESTION_SET_C = "Set C"
    const val SUBJECT_NAME = "Subject"
    var SCORE = 0
}