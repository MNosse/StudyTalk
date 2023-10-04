package br.com.udesc.eso.tcc.studytalk.participant.api

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
import kotlin.jvm.optionals.getOrNull

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ChangeAnAnswerLikeStatusApiTest @Autowired constructor(
    private val answerRepository: AnswerRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository
) {
    var participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    var participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setUp(testInfo: TestInfo) {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/participants"
        val institution1 = institutionRepository.save(InstitutionSchema(name = "Instituição 1"))
        val institution2 = institutionRepository.save(InstitutionSchema(name = "Instituição 2"))
        val participant1 = participantRepository.save(
            ParticipantSchema(
                uid = participant1Uid,
                name = "Mateus Nosse",
                institution = institution1
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

        val answer = answerRepository.save(
            AnswerSchema(
                description = "Descrição da Resposta",
                question = question,
                participant = participant1,
                likeCount = when (testInfo.displayName) {
                    "decreasingLikeCount()" -> 1
                    else -> 0
                }
            )
        )

        participantRepository.save(
            ParticipantSchema(
                uid = participant2Uid,
                name = "Mateus Coelho",
                institution = when (testInfo.displayName) {
                    "withoutPermission()" -> institution2
                    else -> institution1
                },
                likedAnswers = when (testInfo.displayName) {
                    "decreasingLikeCount()" -> mutableListOf(answer)
                    else -> mutableListOf()
                }
            )
        )
    }

    @Test
    fun increasingLikeCount() {
        RestAssured.given()
            .pathParam("participantUid", participant2Uid)
            .pathParam("answerId", 1L)
            .contentType(ContentType.JSON)
            .`when`()
            .put("/{participantUid}/answers/{answerId}/change-like-status/")
            .then()
            .statusCode(HttpStatus.OK.value())
        answerRepository.findById(1L).getOrNull()?.let {
            assert((it.id == 1L && it.likeCount == 1))
        }
    }

    @Test
    fun decreasingLikeCount() {
        RestAssured.given()
            .pathParam("participantUid", participant2Uid)
            .pathParam("answerId", 1L)
            .contentType(ContentType.JSON)
            .`when`()
            .put("/{participantUid}/answers/{answerId}/change-like-status/")
            .then()
            .statusCode(HttpStatus.OK.value())
        answerRepository.findById(1L).getOrNull()?.let {
            assert((it.id == 1L && it.likeCount == 0))
        }
    }

    @Test
    fun withNonExistentAnswer() {
        RestAssured.given()
            .pathParam("participantUid", participant2Uid)
            .pathParam("answerId", 2L)
            .contentType(ContentType.JSON)
            .`when`()
            .put("/{participantUid}/answers/{answerId}/change-like-status/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("A resposta não foi encontrada."))
    }

    @Test
    fun withNonExistentParticipant() {
        RestAssured.given()
            .pathParam("participantUid", participant2Uid + "a")
            .pathParam("answerId", 1L)
            .contentType(ContentType.JSON)
            .`when`()
            .put("/{participantUid}/answers/{answerId}/change-like-status/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O participante não foi encontrado."))
    }
}