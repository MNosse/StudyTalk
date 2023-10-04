package br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller

import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerIsNotFromParticipantException
import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.enrollmentRequest.exception.EnrollmentRequestNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.institution.exception.InstitutionNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionIsNotFromParticipantException
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.report.exception.ReportNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.function.Consumer

abstract class BaseController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(e: MethodArgumentNotValidException): Map<String, String> {
        val errors: MutableMap<String, String> = HashMap()
        e.bindingResult.allErrors.forEach(Consumer { error: ObjectError ->
            val field = (error as FieldError).field
            val message = error.getDefaultMessage()
            errors[field] = message ?: ""
        })
        return errors
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AdministratorNotFoundException::class)
    @ResponseBody
    fun handleAdministratorNotFoundException(ex: AdministratorNotFoundException): ErrorResponse {
        return ErrorResponse(ex.message.orEmpty())
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AnswerIsNotFromParticipantException::class)
    @ResponseBody
    fun handleAnswerIsNotFromParticipantException(ex: AnswerIsNotFromParticipantException): ErrorResponse {
        return ErrorResponse(ex.message.orEmpty())
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AnswerNotFoundException::class)
    @ResponseBody
    fun handleAnswerNotFoundException(ex: AnswerNotFoundException): ErrorResponse {
        return ErrorResponse(ex.message.orEmpty())
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EnrollmentRequestNotFoundException::class)
    @ResponseBody
    fun handleEnrollmentRequestNotFoundException(ex: EnrollmentRequestNotFoundException): ErrorResponse {
        return ErrorResponse(ex.message.orEmpty())
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(InstitutionNotFoundException::class)
    @ResponseBody
    fun handleInstitutionNotFoundException(ex: InstitutionNotFoundException): ErrorResponse {
        return ErrorResponse(ex.message.orEmpty())
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ParticipantNotFoundException::class)
    @ResponseBody
    fun handleParticipantNotFoundException(ex: ParticipantNotFoundException): ErrorResponse {
        return ErrorResponse(ex.message.orEmpty())
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParticipantWithoutPermissionException::class)
    @ResponseBody
    fun handleParticipantWithoutPermissionException(ex: ParticipantWithoutPermissionException): ErrorResponse {
        return ErrorResponse(ex.message.orEmpty())
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParticipantWithoutPrivilegeException::class)
    @ResponseBody
    fun handleParticipantWithoutPrivilegeException(ex: ParticipantWithoutPrivilegeException): ErrorResponse {
        return ErrorResponse(ex.message.orEmpty())
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(QuestionIsNotFromParticipantException::class)
    @ResponseBody
    fun handleQuestionIsNotFromParticipantException(ex: QuestionIsNotFromParticipantException): ErrorResponse {
        return ErrorResponse(ex.message.orEmpty())
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(QuestionNotFoundException::class)
    @ResponseBody
    fun handleQuestionNotFoundException(ex: QuestionNotFoundException): ErrorResponse {
        return ErrorResponse(ex.message.orEmpty())
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ReportNotFoundException::class)
    @ResponseBody
    fun handleReportNotFoundException(ex: ReportNotFoundException): ErrorResponse {
        return ErrorResponse(ex.message.orEmpty())
    }

    data class ErrorResponse(val error: String)
}