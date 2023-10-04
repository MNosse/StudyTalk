package br.com.udesc.eso.tcc.studytalk.answer.api

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.answer.AnswerRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.question.QuestionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.answer.AnswerSchema
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
class UpdateAnswerApiTest @Autowired constructor(
    private val answerRepository: AnswerRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository
) {
    var answerDescription = "Descrição da Resposta"
    var updatedAnswerDescription = "Nova descrição da Resposta"
    var participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    var participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/answers"
        val institution1 = institutionRepository.save(InstitutionSchema(name = "Instituição 1"))
        val institution2 = institutionRepository.save(InstitutionSchema(name = "Instituição 2"))
        val participant1 = participantRepository.save(
            ParticipantSchema(
                uid = participant1Uid,
                name = "Mateus Nosse",
                institution = institution1
            )
        )
        val participant2 = participantRepository.save(
            ParticipantSchema(
                uid = participant2Uid,
                name = "Mateus Coelho",
                institution = when (testInfo.displayName) {
                    "withoutPermission()" -> institution2
                    else -> institution1
                }
            )
        )

        val question = questionRepository.save(
            QuestionSchema(
                title = "Pergunta",
                description = "Descrição da pergunta",
                subjects = mutableListOf(Subject.ARTS),
                participant = participant1,
                institution = institution1
            )
        )
        answerRepository.save(
            AnswerSchema(
                description = answerDescription,
                question = question,
                participant = participant1
            )
        )
    }

    @Test
    fun withValidValues() {
        val requestBody = mapOf(
            "description" to updatedAnswerDescription,
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
    fun withBlankDescription() {
        val requestBody = mapOf(
            "description" to " ",
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
            .body("description", CoreMatchers.equalTo("A descrição deve conter ao menos um caractere."))
    }

    @Test
    fun withOverflowedDescription() {
        val requestBody = mapOf(
            "description" to "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvw",
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
            .body("description", CoreMatchers.equalTo("A descrição não pode ultrapassar 512 caracteres."))
    }

    @Test
    fun withInvalidId() {
        val requestBody = mapOf(
            "description" to updatedAnswerDescription,
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
            .body("error", CoreMatchers.equalTo("A resposta não foi encontrada."))
    }

    @Test
    fun withoutPermission() {
        val requestBody = mapOf(
            "description" to updatedAnswerDescription,
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
            .body("error", CoreMatchers.equalTo("A resposta não pertence ao participante."))
    }
}