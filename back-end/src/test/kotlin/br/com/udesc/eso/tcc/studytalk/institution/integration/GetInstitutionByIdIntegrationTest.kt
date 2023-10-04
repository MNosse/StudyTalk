package br.com.udesc.eso.tcc.studytalk.institution.integration

import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.institution.exception.InstitutionNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.administrator.AdministratorRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.administrator.AdministratorSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.institution.controller.GetInstitutionByIdController
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GetInstitutionByIdIntegrationTest @Autowired constructor(
    private val administratorRepository: AdministratorRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val getInstitutionByIdController: GetInstitutionByIdController
) {
    val administratorUid = "VcZSfuTj8ENjztIccfjbK2KRbHf2"
    val institution1Name = "Instituição 1"
    val institution2Name = "Instituição 2"
    val participantUid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        administratorRepository.save(AdministratorSchema(uid = administratorUid))
        val institution1 = institutionRepository.save(InstitutionSchema(name = institution1Name))
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
        assertDoesNotThrow {
            val response = getInstitutionByIdController.getInstitutionById(
                requestingUid = administratorUid,
                isAdministrator = true,
                id = 1L
            )
            assert(response.id == 1L && response.name == institution1Name && response.registrationCode.isNotBlank())
        }
    }

    @Test
    fun withValidValues2() {
        assertDoesNotThrow {
            val response = getInstitutionByIdController.getInstitutionById(
                requestingUid = participantUid,
                isAdministrator = false,
                id = 1L
            )
            assert(response.id == 1L && response.name == institution1Name && response.registrationCode.isNotBlank())
        }
    }

    @Test
    fun withInvalidId1() {
        assertThrows<InstitutionNotFoundException> {
            getInstitutionByIdController.getInstitutionById(
                requestingUid = administratorUid,
                isAdministrator = true,
                id = 3L
            )
        }
    }

    @Test
    fun withInvalidId2() {
        assertThrows<InstitutionNotFoundException> {
            getInstitutionByIdController.getInstitutionById(
                requestingUid = participantUid,
                isAdministrator = false,
                id = 3L
            )
        }
    }

    @Test
    fun withNonExistentAdministrator() {
        assertThrows<AdministratorNotFoundException> {
            getInstitutionByIdController.getInstitutionById(
                requestingUid = administratorUid + "a",
                isAdministrator = true,
                id = 2L
            )
        }
    }

    @Test
    fun withNonExistentParticipant() {
        assertThrows<ParticipantNotFoundException> {
            getInstitutionByIdController.getInstitutionById(
                requestingUid = participantUid + "a",
                isAdministrator = false,
                id = 2L
            )
        }
    }

    @Test
    fun withoutPermission() {
        assertThrows<ParticipantWithoutPermissionException> {
            getInstitutionByIdController.getInstitutionById(
                requestingUid = participantUid,
                isAdministrator = false,
                id = 1L
            )
        }
    }
}