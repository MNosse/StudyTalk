package br.com.udesc.eso.tcc.studytalk.enrollmentRequest.integration

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.entity.enrollmentRequest.exception.EnrollmentRequestNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.enrollmentRequest.EnrollmentRequestRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.enrollmentRequest.EnrollmentRequestSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller.ApproveEnrollmentRequestController
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ApproveEnrollmentRequestIntegrationTest @Autowired constructor(
    private val institutionRepository: InstitutionRepository,
    private val enrollmentRequestRepository: EnrollmentRequestRepository,
    private val participantRepository: ParticipantRepository,
    private val approveEnrollmentRequestController: ApproveEnrollmentRequestController
) {
    lateinit var institution1: InstitutionSchema
    var participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    var participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        institution1 = institutionRepository.save(InstitutionSchema(name = "Instituição 1"))
        val institution2 = institutionRepository.save(InstitutionSchema(name = "Instituição 2"))
        participantRepository.save(
            ParticipantSchema(
                uid = participant1Uid,
                name = "Mateus Nosse",
                privilege = when (testInfo.displayName) {
                    "withValidValues2()" -> Privilege.TEACHER
                    "withoutPrivilege()" -> Privilege.DEFAULT
                    else -> Privilege.PRINCIPAL
                },
                institution = when (testInfo.displayName) {
                    "withoutPermission()" -> institution2
                    else -> institution1
                }
            )
        )
        val participant = participantRepository.save(
            ParticipantSchema(
                uid = participant2Uid,
                name = "Mateus Coelho"
            )
        )
        enrollmentRequestRepository.save(
            EnrollmentRequestSchema(
                institution = institution1,
                participant = participant
            )
        )

    }

    @Test
    fun withValidValues1() {
        assertDoesNotThrow {
            approveEnrollmentRequestController.approveEnrollmentRequestById(
                id = 1L,
                approverUid = participant1Uid
            )
            assert(enrollmentRequestRepository.findById(1L).isEmpty)
            participantRepository.findByUid(participant2Uid)?.let {
                assert(it.institution == institution1)
            }

        }
    }

    @Test
    fun withValidValues2() {
        assertDoesNotThrow {
            approveEnrollmentRequestController.approveEnrollmentRequestById(
                id = 1L,
                approverUid = participant1Uid
            )
            assert(enrollmentRequestRepository.findById(1L).isEmpty)
            participantRepository.findByUid(participant2Uid)?.let {
                assert(it.institution == institution1)
            }
        }
    }

    @Test
    fun withoutPrivilege() {
        assertThrows<ParticipantWithoutPrivilegeException> {
            approveEnrollmentRequestController.approveEnrollmentRequestById(
                id = 1L,
                approverUid = participant1Uid
            )
        }
    }

    @Test
    fun withoutPermission() {
        assertThrows<ParticipantWithoutPermissionException> {
            approveEnrollmentRequestController.approveEnrollmentRequestById(
                id = 1L,
                approverUid = participant1Uid
            )
        }
    }

    @Test
    fun withInvalidId() {
        assertThrows<EnrollmentRequestNotFoundException> {
            approveEnrollmentRequestController.approveEnrollmentRequestById(
                id = 2L,
                approverUid = participant1Uid
            )
        }
    }

    @Test
    fun withInvalidApproverUid() {
        assertThrows<ParticipantNotFoundException> {
            approveEnrollmentRequestController.approveEnrollmentRequestById(
                id = 2L,
                approverUid = participant1Uid + "a"
            )
        }
    }
}