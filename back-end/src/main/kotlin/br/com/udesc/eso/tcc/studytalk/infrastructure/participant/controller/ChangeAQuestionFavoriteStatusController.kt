package br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionNotFoundException
import br.com.udesc.eso.tcc.studytalk.useCase.participant.ChangeAQuestionFavoriteStatusUseCase
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/participants")
@Validated
class ChangeAQuestionFavoriteStatusController(private val changeAQuestionFavoriteStatusUseCase: ChangeAQuestionFavoriteStatusUseCase) : BaseController() {
    @PutMapping("/{participantUid}/questions/{questionId}/change-favorite-status/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(ParticipantNotFoundException::class, QuestionNotFoundException::class)
    fun changeAQuestionFavoriteStatus(@PathVariable participantUid: String, @PathVariable questionId: Long) {
        changeAQuestionFavoriteStatusUseCase.execute(
            ChangeAQuestionFavoriteStatusUseCase.Input(
                participantUid = participantUid,
                questionId = questionId
            )
        )
    }
}