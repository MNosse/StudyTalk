package br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.gateway

import br.com.udesc.eso.tcc.studytalk.entity.enrollmentRequest.gateway.EnrollmentRequestGateway
import br.com.udesc.eso.tcc.studytalk.entity.enrollmentRequest.model.EnrollmentRequest
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.enrollmentRequest.EnrollmentRequestRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.gateway.converter.convert
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class EnrollmentRequestDatabaseGateway(
    private val enrollmentRequestRepository: EnrollmentRequestRepository,
    private val participantRepository: ParticipantRepository
) : EnrollmentRequestGateway {
    override fun approve(id: Long) {
        enrollmentRequestRepository.findById(id).getOrNull()?.let {
            it.participant.institution = it.institution
            participantRepository.save(it.participant)
            enrollmentRequestRepository.delete(it)
        }
    }

    override fun getAllByInstitutionId(id: Long): MutableList<EnrollmentRequest> {
        return enrollmentRequestRepository.findAllByInstitutionId(id).map {
            convert(it)
        }.toMutableList()
    }

    override fun getById(id: Long): EnrollmentRequest? {
        return enrollmentRequestRepository.findById(id).getOrNull()?.let {
            convert(it)
        }
    }

    override fun getByParticipantId(id: Long): EnrollmentRequest? {
        return enrollmentRequestRepository.findByParticipantId(id)?.let {
            convert(it)
        }
    }

    override fun reprove(id: Long) {
        enrollmentRequestRepository.findById(id).getOrNull()?.let {
            enrollmentRequestRepository.delete(it)
            participantRepository.delete(it.participant)
        }
    }
}