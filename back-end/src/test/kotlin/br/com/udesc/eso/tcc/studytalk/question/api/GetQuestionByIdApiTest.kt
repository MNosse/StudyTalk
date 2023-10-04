package br.com.udesc.eso.tcc.studytalk.question.api

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.entity.question.model.Question
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
class GetQuestionByIdApiTest @Autowired constructor(
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository
) {
    val questionTitle = "Questão 1"
    val questionDescription = "Descrição da Questão 1"
    val questionSubjects = mutableListOf(Subject.MATHEMATICS, Subject.SCIENCE)
    val participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    val participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/questions"
        val institution1 = institutionRepository.save(InstitutionSchema(name = "Instituição 1"))
        val institution2 = institutionRepository.save(InstitutionSchema(name = "Instituição 2"))
        val participant1 = participantRepository.save(
            ParticipantSchema(
                uid = participant1Uid,
                name = "Mateus Nosse",
                institution = institution1
            )
        )
        participantRepository.save(
            ParticipantSchema(
                uid = participant2Uid,
                name = "Mateus Coelho",
                institution = when (testInfo.displayName) {
                    "withoutPermission()" -> institution2
                    else -> institution1
                }
            )
        )
        questionRepository.save(
            QuestionSchema(
                title = questionTitle,
                description = questionDescription,
                subjects = questionSubjects,
                participant = participant1,
                institution = institution1
            )
        )
    }

    @Test
    fun withValidValues() {
        val expectedResponse = Question(
            id = 1L,
            title = questionTitle,
            description = questionDescription,
            subjects = questionSubjects,
            participant = null,
            institution = null
        )

        val actualResponse = RestAssured.given()
            .pathParam("id", 1L)
            .param("participantUid", participant1Uid)
            .`when`()
            .get("/{id}/")
            .then()
            .statusCode(HttpStatus.FOUND.value())
            .contentType(ContentType.JSON)
            .extract()
            .`as`(Question::class.java)

        assert(expectedResponse.id == actualResponse.id)
        assert(expectedResponse.title == actualResponse.title)
        assert(expectedResponse.description == actualResponse.description)
        assert(expectedResponse.subjects == actualResponse.subjects)
    }

    @Test
    fun withInvalidId() {
        RestAssured.given()
            .pathParam("id", 2L)
            .param("participantUid", participant1Uid)
            .`when`()
            .get("/{id}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("A questão não foi encontrada."))
    }

    @Test
    fun withNonExistentParticipant() {
        RestAssured.given()
            .pathParam("id", 1L)
            .param("participantUid", participant1Uid + "a")
            .`when`()
            .get("/{id}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O participante não foi encontrado."))
    }

    @Test
    fun withoutPermission() {
        RestAssured.given()
            .pathParam("id", 1L)
            .param("participantUid", participant2Uid)
            .`when`()
            .get("/{id}/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("error", CoreMatchers.equalTo("O participante não possui a permissão necessária."))
    }
}