package br.com.udesc.eso.tcc.studytalk.core.presentation.activity.base

sealed class BaseScreens(val route: String) {
    object AddEditViewInstitutionScreen : BaseScreens("add_edit_view_institution_screen")
    object AddEditViewQuestionScreen : BaseScreens("add_edit_view_question_screen")
    object CreateParticipantScreen : BaseScreens("create_participant_screen")
    object InstitutionsScreen : BaseScreens("institutions_screen")
    object HomeScreen : BaseScreens("home_screen")
    object ParticipantsScreen : BaseScreens("participants_screen")
    object ProfileScreen : BaseScreens("profile_screen")
    object QuestionsScreen : BaseScreens("questions_screen")
    object WaitingApproveScreen : BaseScreens("waiting_approve_screen")
}