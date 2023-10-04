package br.com.udesc.eso.tcc.studytalk.participant.api

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.administrator.AdministratorRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.administrator.AdministratorSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.UpdateParticipantPrivilegeController
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.*
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
class UpdateParticipantPrivilegeApiTest @Autowired constructor(
    private val administratorRepository: AdministratorRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository
) {
    val administratorUid = "VcZSfuTj8ENjztIccfjbK2KRbHf2"
    val participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    val participant1Name = "Mateus Nosse"
    val participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"
    val participant2Name = "Mateus Coelho"

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setUp(testInfo: TestInfo) {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/participants"
        administratorRepository.save(AdministratorSchema(uid = administratorUid))
        val institution = institutionRepository.save(InstitutionSchema(name = "Instituição 1"))
        participantRepository.save(
            ParticipantSchema(
                uid = participant1Uid,
                name = participant1Name,
                privilege = when (testInfo.displayName) {
                    "withoutPrivilege2()" -> Privilege.TEACHER
                    "withoutPrivilege3()" -> Privilege.DEFAULT
                    else -> Privilege.PRINCIPAL
                },
                institution = institution
            )
        )
        participantRepository.save(
            ParticipantSchema(
                uid = participant2Uid,
                name = participant2Name,
                institution = institution
            )
        )
    }

    @Test
    fun withValidValues1() {
        val requestBody = mapOf(
            "requestingUid" to administratorUid,
            "isAdministrator" to true,
            "privilege" to Privilege.PRINCIPAL
        )
        RestAssured.given()
            .pathParam("participantToBeUpdatedUid", participant2Uid)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{participantToBeUpdatedUid}/privilege/")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun withValidValues2() {
        val requestBody = mapOf(
            "requestingUid" to administratorUid,
            "isAdministrator" to true,
            "privilege" to Privilege.TEACHER
        )
        RestAssured.given()
            .pathParam("participantToBeUpdatedUid", participant2Uid)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{participantToBeUpdatedUid}/privilege/")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun withValidValues3() {
        val requestBody = mapOf(
            "requestingUid" to participant1Uid,
            "isAdministrator" to false,
            "privilege" to Privilege.TEACHER
        )
        RestAssured.given()
            .pathParam("participantToBeUpdatedUid", participant2Uid)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{participantToBeUpdatedUid}/privilege/")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun withoutPrivilege1() {
        val requestBody = mapOf(
            "requestingUid" to participant1Uid,
            "isAdministrator" to false,
            "privilege" to Privilege.PRINCIPAL
        )
        RestAssured.given()
            .pathParam("participantToBeUpdatedUid", participant2Uid)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{participantToBeUpdatedUid}/privilege/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("error", CoreMatchers.equalTo("O participante não possui o privilégio necessário."))
    }

    @Test
    fun withoutPrivilege2() {
        val requestBody = mapOf(
            "requestingUid" to participant1Uid,
            "isAdministrator" to false,
            "privilege" to Privilege.TEACHER
        )
        RestAssured.given()
            .pathParam("participantToBeUpdatedUid", participant2Uid)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{participantToBeUpdatedUid}/privilege/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("error", CoreMatchers.equalTo("O participante não possui o privilégio necessário."))
    }

    @Test
    fun withoutPrivilege3() {
        val requestBody = mapOf(
            "requestingUid" to participant1Uid,
            "isAdministrator" to false,
            "privilege" to Privilege.TEACHER
        )
        RestAssured.given()
            .pathParam("participantToBeUpdatedUid", participant2Uid)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{participantToBeUpdatedUid}/privilege/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("error", CoreMatchers.equalTo("O participante não possui o privilégio necessário."))
    }

    @Test
    fun withNonExistentAdministrator() {
        val requestBody = mapOf(
            "requestingUid" to administratorUid + "a",
            "isAdministrator" to true,
            "privilege" to Privilege.PRINCIPAL
        )
        RestAssured.given()
            .pathParam("participantToBeUpdatedUid", participant2Uid)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{participantToBeUpdatedUid}/privilege/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O administrador não foi encontrado."))
    }

    @Test
    fun withNonExistentParticipant1() {
        val requestBody = mapOf(
            "requestingUid" to participant1Uid + "a",
            "isAdministrator" to false,
            "privilege" to Privilege.TEACHER
        )
        RestAssured.given()
            .pathParam("participantToBeUpdatedUid", participant2Uid)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{participantToBeUpdatedUid}/privilege/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O participante não foi encontrado."))
    }

    @Test
    fun withNonExistentParticipant2() {
        val requestBody = mapOf(
            "requestingUid" to participant1Uid,
            "isAdministrator" to false,
            "privilege" to Privilege.TEACHER
        )
        RestAssured.given()
            .pathParam("participantToBeUpdatedUid", participant2Uid + "a")
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{participantToBeUpdatedUid}/privilege/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O participante não foi encontrado."))
    }
}