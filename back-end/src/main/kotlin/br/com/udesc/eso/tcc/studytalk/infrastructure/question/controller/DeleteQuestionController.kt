package br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionIsNotFromParticipantException
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionNotFoundException
import br.com.udesc.eso.tcc.studytalk.useCase.question.DeleteQuestionUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/questions")
class DeleteQuestionController(private val deleteQuestionUseCase: DeleteQuestionUseCase) : BaseController() {
    @DeleteMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(
        QuestionIsNotFromParticipantException::class,
        QuestionNotFoundException::class
    )
    fun deleteQuestionById(@PathVariable id: Long, @RequestParam(name = "participantUid") participantUid: String) {
        deleteQuestionUseCase.execute(DeleteQuestionUseCase.Input(id = id, participantUid = participantUid))
    }
}