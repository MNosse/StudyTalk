package br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.converter.convert
import br.com.udesc.eso.tcc.studytalk.useCase.question.GetAllQuestionsByInstitutionUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/studytalk/api/questions/institution")
class GetAllQuestionsByInstitutionController(private val getAllQuestionsByInstitutionUseCase: GetAllQuestionsByInstitutionUseCase) : BaseController() {
    @GetMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(ParticipantNotFoundException::class, ParticipantWithoutPermissionException::class)
    fun getAllQuestionsByInstitutionId(
        @PathVariable id: Long,
        @RequestParam(name = "participantUid") participantUid: String
    ): Response {
        return Response(
            convert(
                getAllQuestionsByInstitutionUseCase.execute(
                    GetAllQuestionsByInstitutionUseCase.Input(
                        participantUid = participantUid,
                        institutionId = id
                    )
                ).questions
            )
        )
    }

    data class Response(
        val questions: MutableList<br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.response.Response>
    )
}