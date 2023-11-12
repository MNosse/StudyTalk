package br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.domain.useCase

data class EnrollmentRequestUseCases(
    val approveUseCase: ApproveUseCase,
    val getAllByInstitutionUseCase: GetAllByInstitutionUseCase,
    val reproveUseCase: ReproveUseCase
)