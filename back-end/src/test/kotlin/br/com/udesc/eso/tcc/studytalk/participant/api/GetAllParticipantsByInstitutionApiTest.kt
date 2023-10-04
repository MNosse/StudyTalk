package br.com.udesc.eso.tcc.studytalk.participant.api

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.entity.participant.model.Participant
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.administrator.AdministratorRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.administrator.AdministratorSchema
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
class GetAllParticipantsByInstitutionApiTest @Autowired constructor(
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
    fun setup(testInfo: TestInfo) {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/participants/institution"
        administratorRepository.save(AdministratorSchema(uid = administratorUid))
        val institution = institutionRepository.save(InstitutionSchema(name = "Instituição 1"))
        participantRepository.save(
            ParticipantSchema(
                uid = participant1Uid,
                name = participant1Name,
                privilege = when (testInfo.displayName) {
                    "withoutPrivilege1()" -> Privilege.TEACHER
                    "withoutPrivilege2()" -> Privilege.DEFAULT
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
        val actualResponse = RestAssured.given()
            .pathParam("institutionId", 1L)
            .param("requestingUid", administratorUid)
            .param("isAdministrator", true)
            .`when`()
            .get("/{institutionId}/")
            .then()
            .extract()
            .jsonPath()
            .getList("participants", Participant::class.java)

        actualResponse[0].let {
            assert(it.uid == participant1Uid)
            assert(it.name == participant1Name)
            assert(it.privilege == Privilege.PRINCIPAL)
        }
        actualResponse[1].let {
            assert(it.uid == participant2Uid)
            assert(it.name == participant2Name)
            assert(it.privilege == Privilege.DEFAULT)
        }
    }

    @Test
    fun withValidValues2() {
        val actualResponse = RestAssured.given()
            .pathParam("institutionId", 1L)
            .param("requestingUid", participant1Uid)
            .param("isAdministrator", false)
            .`when`()
            .get("/{institutionId}/")
            .then()
            .extract()
            .jsonPath()
            .getList("participants", Participant::class.java)

        actualResponse[0].let {
            assert(it.uid == participant1Uid)
            assert(it.name == participant1Name)
            assert(it.privilege == Privilege.PRINCIPAL)
        }
        actualResponse[1].let {
            assert(it.uid == participant2Uid)
            assert(it.name == participant2Name)
            assert(it.privilege == Privilege.DEFAULT)
        }
    }

    @Test
    fun withoutPrivilege1() {
        RestAssured.given()
            .pathParam("institutionId", 1L)
            .param("requestingUid", participant1Uid)
            .param("isAdministrator", false)
            .`when`()
            .get("/{institutionId}/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("error", CoreMatchers.equalTo("O participante não possui o privilégio necessário."))
    }

    @Test
    fun withoutPrivilege2() {
        RestAssured.given()
            .pathParam("institutionId", 1L)
            .param("requestingUid", participant1Uid)
            .param("isAdministrator", false)
            .`when`()
            .get("/{institutionId}/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("error", CoreMatchers.equalTo("O participante não possui o privilégio necessário."))
    }

    @Test
    fun withNonExistentAdministrator() {
        RestAssured.given()
            .pathParam("institutionId", 1L)
            .param("requestingUid", administratorUid + "a")
            .param("isAdministrator", true)
            .`when`()
            .get("/{institutionId}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O administrador não foi encontrado."))
    }

    @Test
    fun withNonExistentParticipant() {
        RestAssured.given()
            .pathParam("institutionId", 1L)
            .param("requestingUid", participant1Uid + "a")
            .param("isAdministrator", false)
            .`when`()
            .get("/{institutionId}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O participante não foi encontrado."))
    }
}