package br.com.udesc.eso.tcc.studytalk.participant.api

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
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
class ChangeAQuestionFavoriteStatusApiTest @Autowired constructor(
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

        participantRepository.save(
            ParticipantSchema(
                uid = participant2Uid,
                name = "Mateus Coelho",
                institution = when (testInfo.displayName) {
                    "withoutPermission()" -> institution2
                    else -> institution1
                },
                favoriteQuestions = when (testInfo.displayName) {
                    "removingFromFavorite()" -> mutableListOf(question)
                    else -> mutableListOf()
                }
            )
        )
    }

    @Test
    fun addingToFavorite() {
        RestAssured.given()
            .pathParam("participantUid", participant2Uid)
            .pathParam("questionId", 1L)
            .contentType(ContentType.JSON)
            .`when`()
            .put("/{participantUid}/questions/{questionId}/change-favorite-status/")
            .then()
            .statusCode(HttpStatus.OK.value())
        participantRepository.findByUid(participant2Uid)?.let {
            assert(
                it.id == 2L
                        && it.uid == participant2Uid
                        && containsQuestion(it, 1L)
            )
        }
    }

    @Test
    fun removingFromFavorite() {
        RestAssured.given()
            .pathParam("participantUid", participant2Uid)
            .pathParam("questionId", 1L)
            .contentType(ContentType.JSON)
            .`when`()
            .put("/{participantUid}/questions/{questionId}/change-favorite-status/")
            .then()
            .statusCode(HttpStatus.OK.value())
        participantRepository.findByUid(participant2Uid)?.let {
            assert(
                it.id == 2L
                        && it.uid == participant2Uid
                        && !containsQuestion(it, 1L)
            )
        }
    }

    @Test
    fun withNonExistentQuestion() {
        RestAssured.given()
            .pathParam("participantUid", participant2Uid)
            .pathParam("questionId", 2L)
            .contentType(ContentType.JSON)
            .`when`()
            .put("/{participantUid}/questions/{questionId}/change-favorite-status/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("A questão não foi encontrada."))
    }

    @Test
    fun withNonExistentParticipant() {
        RestAssured.given()
            .pathParam("participantUid", participant2Uid + "a")
            .pathParam("questionId", 1L)
            .contentType(ContentType.JSON)
            .`when`()
            .put("/{participantUid}/questions/{questionId}/change-favorite-status/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O participante não foi encontrado."))
    }

    private fun containsQuestion(participantSchema: ParticipantSchema, questionId: Long): Boolean {
        for (question in participantSchema.favoriteQuestions) {
            if (question.id == questionId) {
                return true
            }
        }
        return false
    }
}