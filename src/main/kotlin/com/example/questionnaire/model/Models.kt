package com.example.questionnaire.model

data class Question(
    val id: Int,
    val text: String
)

data class AnswerRequest(
    val questionId: Int,
    val answer: String,
    val sessionId: String? = null
)

data class ApiResponse(
    val status: String,
    val message: String
)
