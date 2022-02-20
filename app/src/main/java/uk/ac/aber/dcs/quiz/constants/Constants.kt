package uk.ac.aber.dcs.quiz
object Constants {
    // firebase paths
    const val TEACHERS_QUIZ_PATH = "Quizzes"
    const val QUIZ_ID = "quizId"
    const val STUDENTID = "studentId"
    const val QUIZQUESTIONLIST = "quizQuestionList"
    const val STUDENT_QUIZ_PATH = "Student's quizzes"
    const val STUDENT_QUIZ_RESULTS_PATH = "Student's quiz results"
    // Errorchecking
    const val ALPHANUM = "^[a-zA-Z0-9 ]*$"
    const val ALPHANUMSPECIAL = "^[ A-Za-z0-9_@./#&+-]*$"
    const val ALPHANUMQUESTION = "^[ A-Za-z0-9./?&+-]*$"
    const val ALPHA = "[^A-Za-z]"
    const val SUCCESS = "Success"
    const val LENGTHCHECK_5 = 5
    const val LENGTHCHECK_40 = 40
    const val LENGTHCHECK_50 = 50
    const val LENGTHCHECK_100 = 100
    const val FAILED = "Failed"

    // Error messages
    const val INVALID_TITLE = "Please use alphanumeric characters only.\n Check length size(<40)"
    const val INVALID_ONE_ANSWER = "Please use alphanumeric characters only.\n Check length size(<50)"
    const val INVALID_OP_ANSWER = "Please use alphanumeric characters only.\nMake sure answer matches one of the Options.\n Check length size(<50)"
    const val INVALID_Q_TEXT = "Please use alphanumeric characters and arithmetic operators only.\n Check length size(6-100)"
    const val MAX_QUIZ_SIZE = 10

    const val TEACHERSDISCLAIMER = "Preview mode - Marks will not count."
    const val STUDENTSDISCLAIMER = "Pressing next button without answering the question will 0 marks."

}