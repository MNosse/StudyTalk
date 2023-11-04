package br.com.udesc.eso.tcc.studytalk.institution.api

import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.administrator.AdministratorRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.administrator.AdministratorSchema
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
class GetInstitutionByRegistrationCodeApiTest @Autowired constructor(
    private val administratorRepository: AdministratorRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository
) {
    val administratorUid = "VcZSfuTj8ENjztIccfjbK2KRbHf2"
    val institution1Name = "Instituição 1"
    var institution1RegistrationCode = ""
    val institution2Name = "Instituição 2"
    val participantUid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setUp(testInfo: TestInfo) {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/institutions/registration_code"
        administratorRepository.save(AdministratorSchema(uid = administratorUid))
        val institution1 = institutionRepository.save(InstitutionSchema(name = institution1Name))
        institution1RegistrationCode = institution1.registrationCode
        val institution2 = institutionRepository.save(InstitutionSchema(name = institution2Name))
        participantRepository.save(
            ParticipantSchema(
                uid = participantUid,
                name = "Mateus Nosse",
                institution = when (testInfo.displayName) {
                    "withoutPermission()" -> institution2
                    else -> institution1
                }
            )
        )
    }

    @Test
    fun withValidValues1() {
        RestAssured.given()
            .pathParam("registrationCode", institution1RegistrationCode)
            .param("requestingUid", administratorUid)
            .param("isAdministrator", true)
            .`when`()
            .get("/{registrationCode}/")
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .body("id", CoreMatchers.equalTo(1))
            .body("name", CoreMatchers.equalTo(institution1Name))
    }

    @Test
    fun withValidValues2() {
        RestAssured.given()
            .pathParam("registrationCode", institution1RegistrationCode)
            .param("requestingUid", participantUid)
            .param("isAdministrator", false)
            .`when`()
            .get("/{registrationCode}/")
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .body("id", CoreMatchers.equalTo(1))
            .body("name", CoreMatchers.equalTo(institution1Name))
    }

    @Test
    fun withInvalidId1() {
        RestAssured.given()
            .pathParam("registrationCode", institution1RegistrationCode + "a")
            .param("requestingUid", administratorUid)
            .param("isAdministrator", true)
            .`when`()
            .get("/{registrationCode}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("A instituição não foi encontrada."))
    }

    @Test
    fun withInvalidId2() {
        RestAssured.given()
            .pathParam("registrationCode", institution1RegistrationCode + "a")
            .param("requestingUid", participantUid)
            .param("isAdministrator", false)
            .`when`()
            .get("/{registrationCode}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("A instituição não foi encontrada."))
    }

    @Test
    fun withNonExistentAdministrator() {
        RestAssured.given()
            .pathParam("registrationCode", institution1RegistrationCode)
            .param("requestingUid", administratorUid + "a")
            .param("isAdministrator", true)
            .`when`()
            .get("/{registrationCode}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O administrador não foi encontrado."))
    }

    @Test
    fun withNonExistentParticipant() {
        RestAssured.given()
            .pathParam("registrationCode", institution1RegistrationCode)
            .param("requestingUid", participantUid + "a")
            .param("isAdministrator", false)
            .`when`()
            .get("/{registrationCode}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O participante não foi encontrado."))
    }

    @Test
    fun withoutPermission() {
        RestAssured.given()
            .pathParam("registrationCode", institution1RegistrationCode)
            .param("requestingUid", participantUid)
            .param("isAdministrator", false)
            .`when`()
            .get("/{registrationCode}/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("error", CoreMatchers.equalTo("O participante não possui a permissão necessária."))
    }
}