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
class GetAllQuestionsByInstitutionApiTest @Autowired constructor(
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository
) {
    val question1Title = "Questão 1"
    val question1Description = "Descrição da Questão 1"
    val question1Subjects = mutableListOf(Subject.MATHEMATICS, Subject.SCIENCE)
    val question2Title = "Questão 2"
    val question2Description = "Descrição da Questão 2"
    val question2Subjects = mutableListOf(Subject.PHILOSOPHY, Subject.SOCIOLOGY)
    val participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    val participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/questions/institution"
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
                title = question1Title,
                description = question1Description,
                subjects = question1Subjects,
                participant = participant1,
                institution = institution1
            )
        )
        questionRepository.save(
            QuestionSchema(
                title = question2Title,
                description = question2Description,
                subjects = question2Subjects,
                participant = participant1,
                institution = institution1
            )
        )
    }

    @Test
    fun withValidValues() {
        val actualResponse = RestAssured.given()
            .pathParam("id", 1L)
            .param("participantUid", participant1Uid)
            .`when`()
            .get("/{id}/")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList("questions", Question::class.java)

        actualResponse[0].let {
            assert(it.id == 1L)
            assert(it.title == question1Title)
            assert(it.description == question1Description)
            assert(it.subjects == question1Subjects)
        }

        actualResponse[1].let {
            assert(it.id == 2L)
            assert(it.title == question2Title)
            assert(it.description == question2Description)
            assert(it.subjects == question2Subjects)
        }
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