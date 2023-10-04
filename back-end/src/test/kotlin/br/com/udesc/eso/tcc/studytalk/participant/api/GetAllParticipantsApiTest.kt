package br.com.udesc.eso.tcc.studytalk.participant.api

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
class GetAllParticipantsApiTest @Autowired constructor(
    private val administratorRepository: AdministratorRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository
) {
    val administratorUid = "VcZSfuTj8ENjztIccfjbK2KRbHf2"
    val participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    val participant1Name = "Mateus Nosse"
    val participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"
    val participant2Name = "Mateus Coelho"
    val participant3Uid = "VoZSfuTj8ENjtBgP2qKQJKE8p8o1"
    val participant3Name = "Mateus Coelho Nosse"
    val participant4Uid = "AADEiZohc1SacwsccfjbK2KRbHf1"
    val participant4Name = "Mateus Nosse Coelho"

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/participants"
        administratorRepository.save(AdministratorSchema(uid = administratorUid))
        val institution1 = institutionRepository.save(InstitutionSchema(name = "Instituição 1"))
        val institution2 = institutionRepository.save(InstitutionSchema(name = "Instituição 2"))
        participantRepository.save(
            ParticipantSchema(
                uid = participant1Uid,
                name = participant1Name,
                institution = institution1
            )
        )
        participantRepository.save(
            ParticipantSchema(
                uid = participant2Uid,
                name = participant2Name,
                institution = institution1
            )
        )
        participantRepository.save(
            ParticipantSchema(
                uid = participant3Uid,
                name = participant3Name,
                institution = institution2
            )
        )
        participantRepository.save(
            ParticipantSchema(
                uid = participant4Uid,
                name = participant4Name,
                institution = institution2
            )
        )
    }

    @Test
    fun withValidValues() {
        val actualResponse = RestAssured.given()
            .param("administratorUid", administratorUid)
            .`when`()
            .get("/")
            .then()
            .extract()
            .jsonPath()
            .getList("participants", Participant::class.java)

        actualResponse[0].let {
            assert(it.uid == participant1Uid)
            assert(it.name == participant1Name)
            assert(it.institution!!.id == 1L)
        }
        actualResponse[1].let {
            assert(it.uid == participant2Uid)
            assert(it.name == participant2Name)
            assert(it.institution!!.id == 1L)
        }
        actualResponse[2].let {
            assert(it.uid == participant3Uid)
            assert(it.name == participant3Name)
            assert(it.institution!!.id == 2L)
        }
        actualResponse[3].let {
            assert(it.uid == participant4Uid)
            assert(it.name == participant4Name)
            assert(it.institution!!.id == 2L)
        }
    }

    @Test
    fun withNonExistentAdministrator() {
        RestAssured.given()
            .param("administratorUid", administratorUid + "a")
            .`when`()
            .get("/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O administrador não foi encontrado."))
    }
}