package br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase

data class ParticipantUseCases(
    val changeAQuestionFavoriteStatusUseCase: ChangeAQuestionFavoriteStatusUseCase,
    val createUseCase: CreateUseCase,
    val doAQuestionUseCase: DoAQuestionUseCase,
    val getByUidUseCase: GetByUidUseCase
)
