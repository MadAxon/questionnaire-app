package com.example.questionnaire.controller

import com.example.questionnaire.model.AnswerRequest
import com.example.questionnaire.model.ApiResponse
import com.example.questionnaire.model.Question
import com.example.questionnaire.service.QuestionnaireService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["*"])
class QuestionnaireController(
    private val questionnaireService: QuestionnaireService
) {

    @GetMapping("/questions")
    fun getQuestions(): ResponseEntity<List<Question>> {
        return ResponseEntity.ok(questionnaireService.getQuestions())
    }

    @PostMapping("/answers")
    fun postAnswer(@RequestBody request: AnswerRequest): ResponseEntity<ApiResponse> {
        questionnaireService.saveAnswer(request)
        return ResponseEntity.ok(ApiResponse("success", "Answer saved successfully"))
    }

    @GetMapping("/answers")
    fun getAnswers(): ResponseEntity<Map<String, Map<Int, String>>> {
        return ResponseEntity.ok(questionnaireService.getAllAnswers())
    }
}
