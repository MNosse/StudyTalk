package br.com.udesc.eso.tcc.studytalk.enrollmentRequest.api

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.entity.enrollmentRequest.model.EnrollmentRequest
import br.com.udesc.eso.tcc.studytalk.entity.institution.model.Institution
import br.com.udesc.eso.tcc.studytalk.entity.participant.model.Participant
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
class GetAllEnrollmentRequestiesByInstitutionApiTest @Autowired constructor(
    private val institutionRepository: InstitutionRepository,
    private val enrollmentRequestRepository: EnrollmentRequestRepository,
    private val participantRepository: ParticipantRepository
) {
    val institutionName = "Instituição 1"
    var institutionRegistrationCode = ""
    val participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    val participant1Name = "Mateus Nosse"
    val participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"
    val participant2Name = "Mateus Coelho"
    val participant3Uid = "BADEiZohc2SxsBgP2qKQJKE8p8o2"
    val participant3Name = "Mateus Coelho Nosse"

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/enrollment-requesties/institution"
        val institution = institutionRepository.save(InstitutionSchema(name = institutionName))
        institutionRegistrationCode = institution.registrationCode
        participantRepository.save(
            ParticipantSchema(
                uid = participant1Uid,
                name = participant1Name,
                privilege = when (testInfo.displayName) {
                    "withValidValues2()" -> Privilege.TEACHER
                    "withoutPrivilege()" -> Privilege.DEFAULT
                    else -> Privilege.PRINCIPAL
                },
                institution = institution
            )
        )
        val participant2 = participantRepository.save(
            ParticipantSchema(
                uid = participant2Uid,
                name = participant2Name
            )
        )
        val participant3 = participantRepository.save(
            ParticipantSchema(
                uid = participant3Uid,
                name = participant3Name
            )
        )
        enrollmentRequestRepository.save(
            EnrollmentRequestSchema(
                institution = institution,
                participant = participant2
            )
        )
        enrollmentRequestRepository.save(
            EnrollmentRequestSchema(
                institution = institution,
                participant = participant3
            )
        )

    }

    @Test
    fun withValidValues1() {
        val expectedResponse = listOf(
            EnrollmentRequest(
                id = 1L,
                institution = Institution(
                    id = 1L,
                    registrationCode = institutionRegistrationCode,
                    name = institutionName
                ),
                participant = Participant(
                    id = 2L,
                    uid = participant2Uid,
                    name = participant2Name,
                    privilege = Privilege.DEFAULT
                )
            ),
            EnrollmentRequest(
                id = 2L,
                institution = Institution(
                    id = 1L,
                    registrationCode = institutionRegistrationCode,
                    name = institutionName
                ),
                participant = Participant(
                    id = 3L,
                    uid = participant3Uid,
                    name = participant3Name,
                    privilege = Privilege.DEFAULT
                )
            )
        )

        val actualResponse = RestAssured.given()
            .pathParam("id", 1L)
            .param("requestingParticipantUid", participant1Uid)
            .`when`()
            .get("/{id}/")
            .then()
            .statusCode(HttpStatus.FOUND.value())
            .extract()
            .jsonPath()
            .getList("enrollmentRequesties", EnrollmentRequest::class.java)

        assert(expectedResponse == actualResponse)
    }

    @Test
    fun withValidValues2() {
        val expectedResponse = listOf(
            EnrollmentRequest(
                id = 1L,
                institution = Institution(
                    id = 1L,
                    registrationCode = institutionRegistrationCode,
                    name = institutionName
                ),
                participant = Participant(
                    id = 2L,
                    uid = participant2Uid,
                    name = participant2Name,
                    privilege = Privilege.DEFAULT
                )
            ),
            EnrollmentRequest(
                id = 2L,
                institution = Institution(
                    id = 1L,
                    registrationCode = institutionRegistrationCode,
                    name = institutionName
                ),
                participant = Participant(
                    id = 3L,
                    uid = participant3Uid,
                    name = participant3Name,
                    privilege = Privilege.DEFAULT
                )
            )
        )

        val actualResponse = RestAssured.given()
            .pathParam("id", 1L)
            .param("requestingParticipantUid", participant1Uid)
            .`when`()
            .get("/{id}/")
            .then()
            .statusCode(HttpStatus.FOUND.value())
            .extract()
            .jsonPath()
            .getList("enrollmentRequesties", EnrollmentRequest::class.java)

        assert(expectedResponse == actualResponse)
    }

    @Test
    fun withoutPrivilege() {
        RestAssured.given()
            .pathParam("id", 1L)
            .param("requestingParticipantUid", participant1Uid)
            .`when`()
            .get("/{id}/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("error", CoreMatchers.equalTo("O participante não possui o privilégio necessário."))
    }

    @Test
    fun withInvalidRequestingParticipantUid() {
        RestAssured.given()
            .pathParam("id", 1L)
            .param("requestingParticipantUid", participant1Uid + "a")
            .`when`()
            .get("/{id}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O participante não foi encontrado."))
    }
}