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
import br.com.udesc.eso.tcc.studytalk.infrastructure.institution.controller.GetInstitutionByRegistrationCodeController
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GetInstitutionByRegistrationCodeIntegrationTest @Autowired constructor(
    private val administratorRepository: AdministratorRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val getInstitutionByRegistrationCodeController: GetInstitutionByRegistrationCodeController
) {
    val administratorUid = "VcZSfuTj8ENjztIccfjbK2KRbHf2"
    val institution1Name = "Instituição 1"
    var institution1RegistrationCode = ""
    val institution2Name = "Instituição 2"
    var institution2RegistrationCode = ""
    val participantUid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        administratorRepository.save(AdministratorSchema(uid = administratorUid))
        val institution1 = institutionRepository.save(InstitutionSchema(name = institution1Name))
        institution1RegistrationCode = institution1.registrationCode
        val institution2 = institutionRepository.save(InstitutionSchema(name = institution2Name))
        institution2RegistrationCode = institution2.registrationCode
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
            val response = getInstitutionByRegistrationCodeController.getInstitutionByRegistrationCode(
                requestingUid = administratorUid,
                isAdministrator = true,
                registrationCode = institution1RegistrationCode
            )
            assert(response.id == 1L && response.name == institution1Name && response.registrationCode.isNotBlank())
        }
    }

    @Test
    fun withValidValues2() {
        assertDoesNotThrow {
            val response = getInstitutionByRegistrationCodeController.getInstitutionByRegistrationCode(
                requestingUid = participantUid,
                isAdministrator = false,
                registrationCode = institution1RegistrationCode
            )
            assert(response.id == 1L && response.name == institution1Name && response.registrationCode.isNotBlank())
        }
    }

    @Test
    fun withInvalidRegistrationCode1() {
        assertThrows<InstitutionNotFoundException> {
            getInstitutionByRegistrationCodeController.getInstitutionByRegistrationCode(
                requestingUid = administratorUid,
                isAdministrator = true,
                registrationCode = institution1RegistrationCode + "a"
            )
        }
    }

    @Test
    fun withInvalidRegistrationCode2() {
        assertThrows<InstitutionNotFoundException> {
            getInstitutionByRegistrationCodeController.getInstitutionByRegistrationCode(
                requestingUid = participantUid,
                isAdministrator = false,
                registrationCode = institution1RegistrationCode + "a"
            )
        }
    }

    @Test
    fun withNonExistentAdministrator() {
        assertThrows<AdministratorNotFoundException> {
            getInstitutionByRegistrationCodeController.getInstitutionByRegistrationCode(
                requestingUid = administratorUid + "a",
                isAdministrator = true,
                registrationCode = institution1RegistrationCode
            )
        }
    }

    @Test
    fun withNonExistentParticipant() {
        assertThrows<ParticipantNotFoundException> {
            getInstitutionByRegistrationCodeController.getInstitutionByRegistrationCode(
                requestingUid = participantUid + "a",
                isAdministrator = false,
                registrationCode = institution1RegistrationCode
            )
        }
    }

    @Test
    fun withoutPermission() {
        assertThrows<ParticipantWithoutPermissionException> {
            getInstitutionByRegistrationCodeController.getInstitutionByRegistrationCode(
                requestingUid = participantUid,
                isAdministrator = false,
                registrationCode = institution1RegistrationCode
            )
        }
    }
}