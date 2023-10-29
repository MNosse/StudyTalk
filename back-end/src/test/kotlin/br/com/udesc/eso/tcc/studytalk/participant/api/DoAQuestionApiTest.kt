package br.com.udesc.eso.tcc.studytalk.participant.api

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
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
class DoAQuestionApiTest @Autowired constructor(
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository
) {
    val questionTitle = "Questão"
    val questionDescription = "Descrição da Questão"
    val questionSubjects = mutableListOf(Subject.MATHEMATICS, Subject.SCIENCE)
    val participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setUp(testInfo: TestInfo) {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/participants"
        val institution = institutionRepository.save(InstitutionSchema(name = "Instituição 1"))
        participantRepository.save(
            ParticipantSchema(
                uid = participant1Uid,
                name = "Mateus Nosse",
                institution = institution
            )
        )
    }

    @Test
    fun withValidValues() {
        val requestBody = mapOf(
            "title" to questionTitle,
            "description" to questionDescription,
            "subjects" to questionSubjects
        )
        RestAssured.given()
            .pathParam("participantUid", participant1Uid)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post("/{participantUid}/questions/")
            .then()
            .statusCode(HttpStatus.CREATED.value())
    }

    @Test
    fun withBlankValues() {
        val requestBody = mapOf(
            "title" to " ",
            "description" to " ",
            "subjects" to questionSubjects
        )
        RestAssured.given()
            .pathParam("participantUid", participant1Uid)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post("/{participantUid}/questions/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("title", CoreMatchers.equalTo("O título deve conter ao menos um caractere."))
            .body("description", CoreMatchers.equalTo("A descrição deve conter ao menos um caractere."))
    }

    @Test
    fun withOverflowedDescription() {
        val requestBody = mapOf(
            "title" to "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvw",
            "description" to "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvw",
            "subjects" to questionSubjects
        )
        RestAssured.given()
            .pathParam("participantUid", participant1Uid)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post("/{participantUid}/questions/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("title", CoreMatchers.equalTo("O título não pode ultrapassar 256 caracteres."))
            .body("description", CoreMatchers.equalTo("A descrição não pode ultrapassar 512 caracteres."))
    }

    @Test
    fun withNonExistentParticipant() {
        val requestBody = mapOf(
            "title" to questionTitle,
            "description" to questionDescription,
            "subjects" to questionSubjects
        )
        RestAssured.given()
            .pathParam("participantUid", participant1Uid + "a")
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post("/{participantUid}/questions/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O participante não foi encontrado."))
    }
}