package br.com.udesc.eso.tcc.studytalk.report.api

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
class CreateReportApiTest @Autowired constructor(
    private val answerRepository: AnswerRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository
) {
    var participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    var participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"
    var postableId = 0L
    val answerDescription = "Descrição da resposta"
    val questionDescription = "Descrição da pergunta"
    val reportDescription = "Descrição da denúncia"

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setUp(testInfo: TestInfo) {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/reports"
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

        val question = questionRepository.save(
            QuestionSchema(
                title = "Pergunta",
                description = questionDescription,
                subjects = mutableListOf(Subject.MATHEMATICS),
                participant = participant1,
                institution = participant1.institution!!
            )
        )

        postableId = when (testInfo.displayName) {
            "withValidValues2()" -> {
                val answer = answerRepository.save(
                    AnswerSchema(
                        description = answerDescription,
                        question = question,
                        participant = participant1
                    )
                )
                answer.id
            }

            else -> {
                question.id
            }
        }
    }

    @Test
    fun withValidValues1() {
        val requestBody = mapOf(
            "requestingParticipantUid" to participant2Uid,
            "postableId" to postableId,
            "postableType" to "QUESTION",
            "description" to reportDescription
        )
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post("/")
            .then()
            .statusCode(HttpStatus.CREATED.value())
    }

    @Test
    fun withValidValues2() {
        val requestBody = mapOf(
            "requestingParticipantUid" to participant2Uid,
            "postableId" to postableId,
            "postableType" to "ANSWER",
            "description" to reportDescription
        )
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post("/")
            .then()
            .statusCode(HttpStatus.CREATED.value())
    }

    @Test
    fun withInvalidAnswerId() {
        val requestBody = mapOf(
            "requestingParticipantUid" to participant2Uid,
            "postableId" to 50L,
            "postableType" to "ANSWER",
            "description" to reportDescription
        )
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post("/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun withInvalidQuestionId() {
        val requestBody = mapOf(
            "requestingParticipantUid" to participant2Uid,
            "postableId" to 50L,
            "postableType" to "QUESTION",
            "description" to reportDescription
        )
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post("/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun withoutPermission() {
        val requestBody = mapOf(
            "requestingParticipantUid" to participant2Uid,
            "postableId" to postableId,
            "postableType" to "QUESTION",
            "description" to reportDescription
        )
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post("/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("error", CoreMatchers.equalTo("O participante não possui a permissão necessária."))
    }

    @Test
    fun withInvalidRequestingParticipantUid() {
        val requestBody = mapOf(
            "requestingParticipantUid" to participant2Uid + "a",
            "postableId" to postableId,
            "postableType" to "QUESTION",
            "description" to reportDescription
        )
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post("/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O participante não foi encontrado."))
    }
}