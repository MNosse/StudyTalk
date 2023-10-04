package br.com.udesc.eso.tcc.studytalk.participant.api

import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
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
class CreateParticipantApiTest @Autowired constructor(
    private val institutionRepository: InstitutionRepository
) {
    val institutionName = "Instituição"
    var registrationCode = ""
    val participantName = "Mateus Nosse"
    val participantUid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setUp(testInfo: TestInfo) {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/participants"
        val institution = institutionRepository.save(InstitutionSchema(name = institutionName))
        registrationCode = institution.registrationCode
    }

    @Test
    fun withValidName() {
        val requestBody = mapOf(
            "registrationCode" to registrationCode,
            "uid" to participantUid,
            "name" to participantName
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
    fun withBlankName() {
        val requestBody = mapOf(
            "registrationCode" to registrationCode,
            "uid" to participantUid,
            "name" to " "
        )
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post("/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("name", CoreMatchers.equalTo("O nome deve conter ao menos um caractere."))
    }

    @Test
    fun withOverflowedName() {
        val requestBody = mapOf(
            "registrationCode" to registrationCode,
            "uid" to participantUid,
            "name" to "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvw"
        )
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post("/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("name", CoreMatchers.equalTo("O nome não pode ultrapassar 128 caracteres."))
    }

    @Test
    fun withNonExistentInstitution() {
        val requestBody = mapOf(
            "registrationCode" to registrationCode + "a",
            "uid" to participantUid,
            "name" to participantName
        )
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post("/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("A instituição não foi encontrada."))
    }
}