package com.example.questionnaire

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class QuestionnaireControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return list of questions on GET questions`() {
        mockMvc.get("/questions")
            .andExpect {
                status { isOk() }
                content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
                jsonPath("$[0].id") { value(1) }
                jsonPath("$[0].text") { isNotEmpty() }
                jsonPath("$.length()") { value(4) }
            }
    }

    @Test
    fun `should save answer on POST answers`() {
        val payload = """
            {
                "questionId": 1,
                "answer": "Kotlin",
                "sessionId": "test_session_123"
            }
        """.trimIndent()

        mockMvc.post("/answers") {
            contentType = MediaType.APPLICATION_JSON
            content = payload
        }.andExpect {
            status { isOk() }
            jsonPath("$.status") { value("success") }
        }

        // Проверяем, что ответ сохранился в памяти
        mockMvc.get("/answers")
            .andExpect {
                status { isOk() }
                jsonPath("$.test_session_123['1']") { value("Kotlin") }
            }
    }
}
