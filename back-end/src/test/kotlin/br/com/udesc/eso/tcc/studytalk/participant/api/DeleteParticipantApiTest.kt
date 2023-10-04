package br.com.udesc.eso.tcc.studytalk.participant.api

import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
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
class DeleteParticipantApiTest @Autowired constructor(
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository
) {
    val institutionName = "Instituição"
    val participant1Name = "Mateus Nosse"
    val participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    val participant2Name = "Mateus Coelho"
    val participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"
    var registrationCode = ""

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/participants"
        val institution = institutionRepository.save(InstitutionSchema(name = institutionName))
        registrationCode = institution.registrationCode
        participantRepository.save(
            ParticipantSchema(
                uid = participant1Uid,
                name = participant1Name,
                institution = institution
            )
        )
        when (testInfo.displayName) {
            "withoutPermission()" -> {
                participantRepository.save(
                    ParticipantSchema(
                        uid = participant2Uid,
                        name = participant2Name,
                        institution = institution
                    )
                )
            }
        }
    }

    @Test
    fun withValidValues() {
        RestAssured.given()
            .pathParam("participantToBeDeletedUid", participant1Uid)
            .param("requestingParticipantUid", participant1Uid)
            .`when`()
            .delete("/{participantToBeDeletedUid}/")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun withInvalidId() {
        RestAssured.given()
            .pathParam("participantToBeDeletedUid", participant1Uid + "a")
            .param("requestingParticipantUid", participant1Uid + "a")
            .`when`()
            .delete("/{participantToBeDeletedUid}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O participante não foi encontrado."))
    }

    @Test
    fun withoutPermission() {
        RestAssured.given()
            .pathParam("participantToBeDeletedUid", participant1Uid)
            .param("requestingParticipantUid", participant2Uid)
            .`when`()
            .delete("/{participantToBeDeletedUid}/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("error", CoreMatchers.equalTo("O participante não possui a permissão necessária."))
    }
}