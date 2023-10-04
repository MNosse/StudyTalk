package br.com.udesc.eso.tcc.studytalk.enrollmentRequest.integration

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.enrollmentRequest.EnrollmentRequestRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.enrollmentRequest.EnrollmentRequestSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller.GetAllEnrollmentRequestiesByInstitutionController
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GetAllEnrollmentRequestiesByInstitutionIntegrationTest @Autowired constructor(
    private val institutionRepository: InstitutionRepository,
    private val enrollmentRequestRepository: EnrollmentRequestRepository,
    private val participantRepository: ParticipantRepository,
    private val getAllEnrollmentRequestiesByInstitutionController: GetAllEnrollmentRequestiesByInstitutionController
) {
    val participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    val participant1Name = "Mateus Nosse"
    val participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"
    val participant2Name = "Mateus Coelho"
    val participant3Uid = "BADEiZohc2SxsBgP2qKQJKE8p8o2"
    val participant3Name = "Mateis Coelho Nosse"

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        val institution = institutionRepository.save(InstitutionSchema(name = "Instituição 1"))
        participantRepository.save(
            ParticipantSchema(
                uid = participant1Uid,
                name = participant1Name,
                privilege = when (testInfo.displayName) {
                    "withValidValues2()" -> Privilege.TEACHER
                    "withoutPrivilege()" -> Privilege.DEFAULT
                    else -> Privilege.PRINCIPAL
                },
                institution = institution
            )
        )
        val participant2 = participantRepository.save(
            ParticipantSchema(
                uid = participant2Uid,
                name = participant2Name
            )
        )
        val participant3 = participantRepository.save(
            ParticipantSchema(
                uid = participant3Uid,
                name = participant3Name
            )
        )
        enrollmentRequestRepository.save(
            EnrollmentRequestSchema(
                institution = institution,
                participant = participant2
            )
        )
        enrollmentRequestRepository.save(
            EnrollmentRequestSchema(
                institution = institution,
                participant = participant3
            )
        )

    }

    @Test
    fun withValidValues1() {
        assertDoesNotThrow {
            val response = getAllEnrollmentRequestiesByInstitutionController.getAllEnrollmentRequestiesByInstitutionId(
                id = 1L,
                requestingParticipantUid = participant1Uid
            )
            for (enrollmentRequest in response.enrollmentRequesties) {
                enrollmentRequest.participant.let {
                    assert(
                        (it.uid == participant2Uid && it.name == participant2Name)
                                || (it.uid == participant3Uid && it.name == participant3Name)
                                && it.privilege == Privilege.DEFAULT
                    )
                }
            }
        }
    }

    @Test
    fun withValidValues2() {
        assertDoesNotThrow {
            val response = getAllEnrollmentRequestiesByInstitutionController.getAllEnrollmentRequestiesByInstitutionId(
                id = 1L,
                requestingParticipantUid = participant1Uid
            )
            for (enrollmentRequest in response.enrollmentRequesties) {
                enrollmentRequest.participant.let {
                    assert(
                        (it.uid == participant2Uid && it.name == participant2Name)
                                || (it.uid == participant3Uid && it.name == participant3Name)
                                && it.privilege == Privilege.DEFAULT
                    )
                }
            }
        }
    }

    @Test
    fun withoutPrivilege() {
        assertThrows<ParticipantWithoutPrivilegeException> {
            getAllEnrollmentRequestiesByInstitutionController.getAllEnrollmentRequestiesByInstitutionId(
                id = 1L,
                requestingParticipantUid = participant1Uid
            )
        }
    }

    @Test
    fun withInvalidRequestingParticipantUid() {
        assertThrows<ParticipantNotFoundException> {
            getAllEnrollmentRequestiesByInstitutionController.getAllEnrollmentRequestiesByInstitutionId(
                id = 2L,
                requestingParticipantUid = participant1Uid + "a"
            )
        }
    }
}