package br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerIsNotFromParticipantException
import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerNotFoundException
import br.com.udesc.eso.tcc.studytalk.useCase.answer.DeleteAnswerUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/answers")
class DeleteAnswerController(private val deleteAnswerUseCase: DeleteAnswerUseCase) : BaseController() {
    @DeleteMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(
        AnswerIsNotFromParticipantException::class,
        AnswerNotFoundException::class
    )
    fun deleteAnswerById(@PathVariable id: Long, @RequestParam(name = "participantUid") participantUid: String) {
        deleteAnswerUseCase.execute(DeleteAnswerUseCase.Input(id, participantUid))
    }
}