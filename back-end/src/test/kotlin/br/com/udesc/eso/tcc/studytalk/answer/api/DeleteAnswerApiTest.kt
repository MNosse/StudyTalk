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
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
class DeleteAnswerApiTest @Autowired constructor(
    private val answerRepository: AnswerRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository
) {
    var participantUid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    var answerDescription = "Resposta 1"

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setup() {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/answers"
        val institution = institutionRepository.save(InstitutionSchema(name = "Instituição 1"))
        val participant = participantRepository.save(
            ParticipantSchema(
                uid = participantUid,
                name = "Mateus Nosse",
                institution = institution
            )
        )
        val question = questionRepository.save(
            QuestionSchema(
                title = "Pergunta 1",
                description = "Descrição da pergunta 1",
                subjects = mutableListOf(Subject.ARTS),
                participant = participant,
                institution = institution
            )
        )
        answerRepository.save(
            AnswerSchema(
                description = answerDescription,
                question = question,
                participant = participant
            )
        )
    }

    @Test
    fun withValidValues() {
        RestAssured.given()
            .pathParam("id", 1L)
            .param("participantUid", participantUid)
            .`when`()
            .delete("/{id}/")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun withInvalidId() {
        RestAssured.given()
            .pathParam("id", 2L)
            .param("participantUid", participantUid)
            .`when`()
            .delete("/{id}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("A resposta não foi encontrada."))
    }

    @Test
    fun withInvalidParticipant() {
        RestAssured.given()
            .pathParam("id", 1L)
            .param("participantUid", participantUid + "a")
            .`when`()
            .delete("/{id}/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("error", CoreMatchers.equalTo("A resposta não pertence ao participante."))
    }
}