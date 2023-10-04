package br.com.udesc.eso.tcc.studytalk.useCase.institution

import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.administrator.gateway.AdministratorGateway
import br.com.udesc.eso.tcc.studytalk.entity.institution.exception.InstitutionNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.institution.gateway.InstitutionGateway
import br.com.udesc.eso.tcc.studytalk.entity.institution.model.Institution
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import org.springframework.stereotype.Service

@Service
class GetInstitutionByRegistrationCodeUseCase(
    private val administratorGateway: AdministratorGateway,
    private val institutionGateway: InstitutionGateway,
    private val participantGateway: ParticipantGateway
) {
    @Throws(
        AdministratorNotFoundException::class,
        InstitutionNotFoundException::class,
        ParticipantNotFoundException::class,
        ParticipantWithoutPermissionException::class
    )
    fun execute(input: Input): Output {
        if (input.isAdministrator) {
            administratorGateway.getByUid(input.requestingUid)?.let {
                institutionGateway.getByRegistrationCode(input.registrationCode)?.let {
                    return Output(it)
                } ?: throw InstitutionNotFoundException()
            } ?: throw AdministratorNotFoundException()
        } else participantGateway.getByUid(input.requestingUid)?.let { participant ->
            institutionGateway.getByRegistrationCode(input.registrationCode)?.let {
                if (participant.institution!!.registrationCode == input.registrationCode) {
                    return Output(it)
                } else throw ParticipantWithoutPermissionException()
            } ?: throw InstitutionNotFoundException()
        } ?: throw ParticipantNotFoundException()
    }

    data class Input(
        val requestingUid: String,
        val isAdministrator: Boolean,
        val registrationCode: String
    )

    data class Output(val institution: Institution)
}