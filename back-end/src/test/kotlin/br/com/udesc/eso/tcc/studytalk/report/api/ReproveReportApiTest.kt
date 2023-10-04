package br.com.udesc.eso.tcc.studytalk.report.api

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.answer.AnswerRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.question.QuestionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.report.ReportRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.answer.AnswerSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.report.ReportSchema
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
class ReproveReportApiTest @Autowired constructor(
    private val answerRepository: AnswerRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository,
    private val reportRepository: ReportRepository
) {
    var participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    var participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"
    val answerDescription = "Descrição da resposta"
    val questionDescription = "Descrição da pergunta"
    val reportDescription = "Descrição da denúncia"

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/reports/reprove"
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
                privilege = when (testInfo.displayName) {
                    "withValidValues2()" -> Privilege.TEACHER
                    "withoutPrivilege()" -> Privilege.DEFAULT
                    else -> Privilege.PRINCIPAL
                },
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

        val postable = when (testInfo.displayName) {
            "withValidValues3()" -> answerRepository.save(
                AnswerSchema(
                    description = answerDescription,
                    question = question,
                    participant = participant1
                )
            )

            else -> question
        }
        reportRepository.save(
            ReportSchema(
                postable = postable,
                description = reportDescription,
                institution = institution1
            )
        )

    }

    @Test
    fun withValidValues1() {
        RestAssured.given()
            .pathParam("id", 1L)
            .param("reproverUid", participant2Uid)
            .`when`()
            .delete("/{id}/")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun withValidValues2() {
        RestAssured.given()
            .pathParam("id", 1L)
            .param("reproverUid", participant2Uid)
            .`when`()
            .delete("/{id}/")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun withValidValues3() {
        RestAssured.given()
            .pathParam("id", 1L)
            .param("reproverUid", participant2Uid)
            .`when`()
            .delete("/{id}/")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun withoutPrivilege() {
        RestAssured.given()
            .pathParam("id", 1L)
            .param("reproverUid", participant2Uid)
            .`when`()
            .delete("/{id}/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("error", CoreMatchers.equalTo("O participante não possui o privilégio necessário."))
    }

    @Test
    fun withoutPermission() {
        RestAssured.given()
            .pathParam("id", 1L)
            .param("reproverUid", participant2Uid)
            .`when`()
            .delete("/{id}/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("error", CoreMatchers.equalTo("O participante não possui a permissão necessária."))
    }

    @Test
    fun withInvalidId() {
        RestAssured.given()
            .pathParam("id", 2L)
            .param("reproverUid", participant2Uid)
            .`when`()
            .delete("/{id}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("A denúncia não foi encontrada."))
    }

    @Test
    fun withInvalidReproverUid() {
        RestAssured.given()
            .pathParam("id", 1L)
            .param("reproverUid", participant2Uid + "a")
            .`when`()
            .delete("/{id}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O participante não foi encontrado."))
    }
}