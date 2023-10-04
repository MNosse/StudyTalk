package br.com.udesc.eso.tcc.studytalk.participant.api

import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
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
class UpdateParticipantApiTest @Autowired constructor(
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository
) {
    val institutionName = "Instituição"
    val participantName = "Mateus Nosse"
    val participantUid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    val updateParticipantName = "Mateus Coelho Nosse"

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setUp(testInfo: TestInfo) {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/participants"
        val institution = institutionRepository.save(InstitutionSchema(name = institutionName))
        participantRepository.save(
            ParticipantSchema(
                uid = participantUid,
                name = participantName,
                institution = institution
            )
        )
    }

    @Test
    fun withValidName() {
        val requestBody = mapOf(
            "requestingParticipantUid" to participantUid,
            "name" to updateParticipantName
        )
        RestAssured.given()
            .pathParam("participantToBeUpdatedUid", participantUid)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{participantToBeUpdatedUid}/")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun withBlankName() {
        val requestBody = mapOf(
            "requestingParticipantUid" to participantUid,
            "name" to " "
        )
        RestAssured.given()
            .pathParam("participantToBeUpdatedUid", participantUid)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{participantToBeUpdatedUid}/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("name", CoreMatchers.equalTo("O nome deve conter ao menos um caractere."))
    }

    @Test
    fun withOverflowedName() {
        val requestBody = mapOf(
            "requestingParticipantUid" to participantUid,
            "name" to "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvw"
        )
        RestAssured.given()
            .pathParam("participantToBeUpdatedUid", participantUid)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{participantToBeUpdatedUid}/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("name", CoreMatchers.equalTo("O nome não pode ultrapassar 128 caracteres."))
    }

    @Test
    fun withNonExixtentParticipant() {
        val requestBody = mapOf(
            "requestingParticipantUid" to participantUid + "a",
            "name" to updateParticipantName
        )
        RestAssured.given()
            .pathParam("participantToBeUpdatedUid", participantUid + "a")
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{participantToBeUpdatedUid}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O participante não foi encontrado."))
    }

    @Test
    fun withoutPermission() {
        val requestBody = mapOf(
            "requestingParticipantUid" to participantUid,
            "name" to updateParticipantName
        )
        RestAssured.given()
            .pathParam("participantToBeUpdatedUid", participantUid + "a")
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{participantToBeUpdatedUid}/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("error", CoreMatchers.equalTo("O participante não possui a permissão necessária."))
    }
}