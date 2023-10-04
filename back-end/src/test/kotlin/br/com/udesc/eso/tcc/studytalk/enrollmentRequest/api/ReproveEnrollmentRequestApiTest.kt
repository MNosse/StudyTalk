package br.com.udesc.eso.tcc.studytalk.enrollmentRequest.api

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.enrollmentRequest.EnrollmentRequestRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.enrollmentRequest.EnrollmentRequestSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
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
class ReproveEnrollmentRequestApiTest @Autowired constructor(
    private val institutionRepository: InstitutionRepository,
    private val enrollmentRequestRepository: EnrollmentRequestRepository,
    private val participantRepository: ParticipantRepository
) {
    lateinit var institution1: InstitutionSchema
    var participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    var participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/enrollment-requesties/reprove"
        institution1 = institutionRepository.save(InstitutionSchema(name = "Instituição 1"))
        val institution2 = institutionRepository.save(InstitutionSchema(name = "Instituição 2"))
        participantRepository.save(
            ParticipantSchema(
                uid = participant1Uid,
                name = "Mateus Nosse",
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
        val participant = participantRepository.save(
            ParticipantSchema(
                uid = participant2Uid,
                name = "Mateus Coelho"
            )
        )
        enrollmentRequestRepository.save(
            EnrollmentRequestSchema(
                institution = institution1,
                participant = participant
            )
        )

    }

    @Test
    fun withValidValues1() {
        RestAssured.given()
            .pathParam("id", 1L)
            .param("reproverUid", participant1Uid)
            .`when`()
            .delete("/{id}/")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun withValidValues2() {
        RestAssured.given()
            .pathParam("id", 1L)
            .param("reproverUid", participant1Uid)
            .`when`()
            .delete("/{id}/")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun withoutPrivilege() {
        RestAssured.given()
            .pathParam("id", 1L)
            .param("reproverUid", participant1Uid)
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
            .param("reproverUid", participant1Uid)
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
            .param("reproverUid", participant1Uid)
            .`when`()
            .delete("/{id}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("A solicitação de cadastro não foi encontrada."))
    }

    @Test
    fun withInvalidReproverUid() {
        RestAssured.given()
            .pathParam("id", 1L)
            .param("reproverUid", participant1Uid + "a")
            .`when`()
            .delete("/{id}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O participante não foi encontrado."))
    }
}