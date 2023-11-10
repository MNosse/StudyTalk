package br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.useCase

data class QuestionUseCases(
    val deleteUseCase: DeleteUseCase,
    val getAllByInstitutionUseCase: GetAllByInstitutionUseCase,
    val getByIdUseCase: GetByIdUseCase,
    val updateUseCase: UpdateUseCase
)
