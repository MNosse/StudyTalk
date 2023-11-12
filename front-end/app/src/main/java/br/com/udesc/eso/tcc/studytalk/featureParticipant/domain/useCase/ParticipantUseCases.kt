package br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase

data class ParticipantUseCases(
    val answerAQuestionUseCase: AnswerAQuestionUseCase,
    val changeAQuestionFavoriteStatusUseCase: ChangeAQuestionFavoriteStatusUseCase,
    val changeAnAnswerLikeStatusUseCase: ChangeAnAnswerLikeStatusUseCase,
    val createUseCase: CreateUseCase,
    val doAQuestionUseCase: DoAQuestionUseCase,
    val getAllByInstitutionUseCase: GetAllByInstitutionUseCase,
    val getAllUseCase: GetAllUseCase,
    val getByUidUseCase: GetByUidUseCase
)
