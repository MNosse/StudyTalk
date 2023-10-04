package br.com.udesc.eso.tcc.studytalk.question.api

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionIsNotFromParticipantException
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.question.QuestionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UpdateQuestionApiTest @Autowired constructor(
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository
) {
    val questionTitle = "Questão"
    val questionDescription = "Descrição da Questão"
    val questionSubjects = mutableListOf(Subject.MATHEMATICS, Subject.SCIENCE)
    val updatedQuestionTitle = "Nova Questão"
    val updatedQuestionDescription = "Nova descrição da Questão"
    val updatedQuestionSubjects = mutableListOf(Subject.PHILOSOPHY, Subject.SOCIOLOGY)
    val participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    val participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/questions"
        val institution = institutionRepository.save(InstitutionSchema(name = "Instituição 1"))
        val participant = participantRepository.save(
            ParticipantSchema(
                uid = participant1Uid,
                name = "Mateus Nosse",
                institution = institution
            )
        )
        questionRepository.save(
            QuestionSchema(
                title = questionTitle,
                description = questionDescription,
                subjects = questionSubjects,
                participant = participant,
                institution = institution
            )
        )
    }

    @Test
    fun withValidValues() {
        val requestBody = mapOf(
            "title" to updatedQuestionTitle,
            "description" to updatedQuestionDescription,
            "subjects" to updatedQuestionSubjects,
            "participantUid" to participant1Uid
        )
        RestAssured.given()
            .pathParam("id", 1L)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{id}/")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun withBlankValues() {
        val requestBody = mapOf(
            "title" to " ",
            "description" to " ",
            "subjects" to null,
            "participantUid" to participant1Uid
        )
        RestAssured.given()
            .pathParam("id", 1L)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{id}/")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun withOverflowedValues() {
        val requestBody = mapOf(
            "title" to "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvw",
            "description" to "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvw",
            "subjects" to updatedQuestionSubjects,
            "participantUid" to participant1Uid
        )
        RestAssured.given()
            .pathParam("id", 1L)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{id}/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("title", CoreMatchers.equalTo("O título não pode ultrapassar 256 caracteres."))
            .body("description", CoreMatchers.equalTo("A descrição não pode ultrapassar 512 caracteres."))
    }

    @Test
    fun withInvalidId() {
        val requestBody = mapOf(
            "title" to updatedQuestionTitle,
            "description" to updatedQuestionDescription,
            "subjects" to updatedQuestionSubjects,
            "participantUid" to participant1Uid
        )
        RestAssured.given()
            .pathParam("id", 2L)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{id}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("A questão não foi encontrada."))

    }

    @Test
    fun withoutPermission() {
        val requestBody = mapOf(
            "title" to updatedQuestionTitle,
            "description" to updatedQuestionDescription,
            "subjects" to updatedQuestionSubjects,
            "participantUid" to participant1Uid + "a"
        )
        RestAssured.given()
            .pathParam("id", 1L)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{id}/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("error", CoreMatchers.equalTo("A questão não pertence ao participante."))
    }
}