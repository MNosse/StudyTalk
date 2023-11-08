package br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.useCase

data class InstitutionUseCases(
    val createUseCase: CreateUseCase,
    val deleteUseCase: DeleteUseCase,
    val getAllUseCase: GetAllUseCase,
    val getByIdUseCase: GetByIdUseCase,
    val updateUseCase: UpdateUseCase
)