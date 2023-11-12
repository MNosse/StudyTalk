package br.com.udesc.eso.tcc.studytalk.featureAnswer.domain.useCase

data class AnswerUseCases(
    val deleteUseCase: DeleteUseCase,
    val getAllByQuestionUseCase: GetAllByQuestionUseCase,
    val getByIdUseCase: GetByIdUseCase,
    val updateUseCase: UpdateUseCase
)