package br.com.udesc.eso.tcc.studytalk.participant.integration

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
import br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.GetAllParticipantsByInstitutionIdController
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GetAllParticipantsByInstitutionIntegrationTest @Autowired constructor(
    private val administratorRepository: AdministratorRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val getAllParticipantsByInstitutionIdController: GetAllParticipantsByInstitutionIdController
) {
    val administratorUid = "VcZSfuTj8ENjztIccfjbK2KRbHf2"
    val participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    val participant1Name = "Mateus Nosse"
    val participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"
    val participant2Name = "Mateus Coelho"

    @BeforeEach
    fun setup(testInfo: TestInfo) {
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
        assertDoesNotThrow {
            val response = getAllParticipantsByInstitutionIdController.getAllParticipantsByInstitutionId(
                institutionId = 1L,
                requestingUid = administratorUid,
                isAdministrator = true
            )
            for (participant in response.participants) {
                assert(
                    (participant.uid == participant1Uid && participant.name == participant1Name && participant.privilege == Privilege.PRINCIPAL)
                            || (participant.uid == participant2Uid && participant.name == participant2Name && participant.privilege == Privilege.DEFAULT)
                )
            }
        }
    }

    @Test
    fun withValidValues2() {
        assertDoesNotThrow {
            val response = getAllParticipantsByInstitutionIdController.getAllParticipantsByInstitutionId(
                institutionId = 1L,
                requestingUid = participant1Uid,
                isAdministrator = false
            )
            for (participant in response.participants) {
                assert(
                    (participant.uid == participant1Uid && participant.name == participant1Name && participant.privilege == Privilege.PRINCIPAL)
                            || (participant.uid == participant2Uid && participant.name == participant2Name && participant.privilege == Privilege.DEFAULT)
                )
            }
        }
    }

    @Test
    fun withoutPrivilege1() {
        assertThrows<ParticipantWithoutPrivilegeException> {
            getAllParticipantsByInstitutionIdController.getAllParticipantsByInstitutionId(
                institutionId = 1L,
                requestingUid = participant1Uid,
                isAdministrator = false
            )
        }
    }

    @Test
    fun withoutPrivilege2() {
        assertThrows<ParticipantWithoutPrivilegeException> {
            getAllParticipantsByInstitutionIdController.getAllParticipantsByInstitutionId(
                institutionId = 1L,
                requestingUid = participant1Uid,
                isAdministrator = false
            )
        }
    }

    @Test
    fun withNonExistentAdministrator() {
        assertThrows<AdministratorNotFoundException> {
            getAllParticipantsByInstitutionIdController.getAllParticipantsByInstitutionId(
                institutionId = 1L,
                requestingUid = administratorUid + "a",
                isAdministrator = true
            )
        }
    }

    @Test
    fun withNonExistentParticipant() {
        assertThrows<ParticipantNotFoundException> {
            getAllParticipantsByInstitutionIdController.getAllParticipantsByInstitutionId(
                institutionId = 1L,
                requestingUid = participant1Uid + "a",
                isAdministrator = false
            )
        }
    }
}