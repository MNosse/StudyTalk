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
import br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.UpdateParticipantPrivilegeController
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.jvm.optionals.getOrNull

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UpdateParticipantPrivilegeIntegrationTest @Autowired constructor(
    private val administratorRepository: AdministratorRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val updateParticipantPrivilegeController: UpdateParticipantPrivilegeController
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
        assertDoesNotThrow {
            updateParticipantPrivilegeController.updateParticipantPrivilege(
                participantToBeUpdatedUid = participant2Uid,
                UpdateParticipantPrivilegeController.Request(
                    requestingUid = administratorUid,
                    isAdministrator = true,
                    privilege = Privilege.PRINCIPAL
                )
            )
            participantRepository.findById(2L).getOrNull()?.let {
                assert(
                    it.id == 2L
                            && it.uid == participant2Uid
                            && it.name == participant2Name
                            && it.privilege == Privilege.PRINCIPAL
                )
            }
        }
    }

    @Test
    fun withValidValues2() {
        assertDoesNotThrow {
            updateParticipantPrivilegeController.updateParticipantPrivilege(
                participantToBeUpdatedUid = participant2Uid,
                UpdateParticipantPrivilegeController.Request(
                    requestingUid = administratorUid,
                    isAdministrator = true,
                    privilege = Privilege.TEACHER
                )
            )
            participantRepository.findById(2L).getOrNull()?.let {
                assert(
                    it.id == 2L
                            && it.uid == participant2Uid
                            && it.name == participant2Name
                            && it.privilege == Privilege.TEACHER
                )
            }
        }
    }

    @Test
    fun withValidValues3() {
        assertDoesNotThrow {
            updateParticipantPrivilegeController.updateParticipantPrivilege(
                participantToBeUpdatedUid = participant2Uid,
                UpdateParticipantPrivilegeController.Request(
                    requestingUid = participant1Uid,
                    isAdministrator = false,
                    privilege = Privilege.TEACHER
                )
            )
            participantRepository.findById(2L).getOrNull()?.let {
                assert(
                    it.id == 2L
                            && it.uid == participant2Uid
                            && it.name == participant2Name
                            && it.privilege == Privilege.TEACHER
                )
            }
        }
    }

    @Test
    fun withoutPrivilege1() {
        assertThrows<ParticipantWithoutPrivilegeException> {
            updateParticipantPrivilegeController.updateParticipantPrivilege(
                participantToBeUpdatedUid = participant2Uid,
                UpdateParticipantPrivilegeController.Request(
                    requestingUid = participant1Uid,
                    isAdministrator = false,
                    privilege = Privilege.PRINCIPAL
                )
            )
        }
    }

    @Test
    fun withoutPrivilege2() {
        assertThrows<ParticipantWithoutPrivilegeException> {
            updateParticipantPrivilegeController.updateParticipantPrivilege(
                participantToBeUpdatedUid = participant2Uid,
                UpdateParticipantPrivilegeController.Request(
                    requestingUid = participant1Uid,
                    isAdministrator = false,
                    privilege = Privilege.TEACHER
                )
            )
        }
    }

    @Test
    fun withoutPrivilege3() {
        assertThrows<ParticipantWithoutPrivilegeException> {
            updateParticipantPrivilegeController.updateParticipantPrivilege(
                participantToBeUpdatedUid = participant2Uid,
                UpdateParticipantPrivilegeController.Request(
                    requestingUid = participant1Uid,
                    isAdministrator = false,
                    privilege = Privilege.TEACHER
                )
            )
        }
    }

    @Test
    fun withNonExistentAdministrator() {
        assertThrows<AdministratorNotFoundException> {
            updateParticipantPrivilegeController.updateParticipantPrivilege(
                participantToBeUpdatedUid = participant2Uid,
                UpdateParticipantPrivilegeController.Request(
                    requestingUid = administratorUid + "a",
                    isAdministrator = true,
                    privilege = Privilege.PRINCIPAL
                )
            )
        }
    }

    @Test
    fun withNonExistentParticipant1() {
        assertThrows<ParticipantNotFoundException> {
            updateParticipantPrivilegeController.updateParticipantPrivilege(
                participantToBeUpdatedUid = participant2Uid,
                UpdateParticipantPrivilegeController.Request(
                    requestingUid = participant1Uid + "a",
                    isAdministrator = false,
                    privilege = Privilege.TEACHER
                )
            )
        }
    }

    @Test
    fun withNonExistentParticipant2() {
        assertThrows<ParticipantNotFoundException> {
            updateParticipantPrivilegeController.updateParticipantPrivilege(
                participantToBeUpdatedUid = participant2Uid + "a",
                UpdateParticipantPrivilegeController.Request(
                    requestingUid = participant1Uid,
                    isAdministrator = false,
                    privilege = Privilege.TEACHER
                )
            )
        }
    }
}