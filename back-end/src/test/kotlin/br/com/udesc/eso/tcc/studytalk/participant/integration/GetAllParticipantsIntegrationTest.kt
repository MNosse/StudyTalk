package br.com.udesc.eso.tcc.studytalk.participant.integration

import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.administrator.AdministratorRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.administrator.AdministratorSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.GetAllParticipantsController
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GetAllParticipantsIntegrationTest @Autowired constructor(
    private val administratorRepository: AdministratorRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val getAllParticipantsController: GetAllParticipantsController
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

    @BeforeEach
    fun setup(testInfo: TestInfo) {
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
        assertDoesNotThrow {
            val response = getAllParticipantsController.getAllParticipants(
                administratorUid = administratorUid,
            )
            for (participant in response.participants) {
                assert(
                    (participant.uid == participant1Uid && participant.name == participant1Name && participant.institution!!.id == 1L)
                            || (participant.uid == participant2Uid && participant.name == participant2Name && participant.institution!!.id == 1L)
                            || (participant.uid == participant3Uid && participant.name == participant3Name && participant.institution!!.id == 2L)
                            || (participant.uid == participant4Uid && participant.name == participant4Name && participant.institution!!.id == 2L)
                )
            }
        }
    }

    @Test
    fun withNonExistentAdministrator() {
        assertThrows<AdministratorNotFoundException> {
            getAllParticipantsController.getAllParticipants(
                administratorUid = administratorUid + "a",
            )
        }
    }
}