package com.example.questionnaire.service

import com.example.questionnaire.model.AnswerRequest
import com.example.questionnaire.model.Question
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class QuestionnaireService {

    private val questions: List<Question> = listOf(
        Question(1, "Какой ваш любимый язык программирования?"),
        Question(2, "Какой фреймворк вы предпочитаете для бэкенда?"),
        Question(3, "Сколько лет вы занимаетесь разработкой?"),
        Question(4, "Какой формат работы вы предпочитаете (удалёнка, офис или гибрид)?")
    )

    // Хранение ответов в памяти: sessionId -> (questionId -> answer)
    private val answersStore = ConcurrentHashMap<String, ConcurrentHashMap<Int, String>>()

    fun getQuestions(): List<Question> = questions

    fun saveAnswer(request: AnswerRequest) {
        val sessionKey = if (request.sessionId.isNullOrBlank()) "default_session" else request.sessionId
        val sessionAnswers = answersStore.computeIfAbsent(sessionKey) { ConcurrentHashMap() }
        sessionAnswers[request.questionId] = request.answer
    }

    fun getAllAnswers(): Map<String, Map<Int, String>> = answersStore
}
