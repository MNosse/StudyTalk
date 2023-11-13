package br.com.udesc.eso.tcc.studytalk.core.presentation.activity.base

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import br.com.udesc.eso.tcc.studytalk.core.domain.model.Privilege
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.BottomNavigationItem
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.model.Administrator
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.useCase.AdministratorUseCases
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.model.Participant
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.ParticipantUseCases
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(
    private val administratorUseCases: AdministratorUseCases,
    private val participantUseCases: ParticipantUseCases
) : ViewModel() {

    lateinit var bottomNavigationItens: List<BottomNavigationItem>

    private val _route = mutableStateOf("")
    val route: State<String> = _route

    init {
        runBlocking {
            setupByCurrentUid()
        }
    }

    fun onEvent(event: BaseEvent) {
        when (event) {
            is BaseEvent.SignOut -> {
                signOut()
            }
        }
    }

    private suspend fun setupByCurrentUid() {
        FirebaseAuth.getInstance().uid?.let { uid ->
            if (uid.isNotBlank()) {
                administratorUseCases.getByUidUseCase(
                    br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.useCase.GetByUidUseCase.Input(
                        uid = uid
                    )
                ).result.let {
                    if (it.isSuccess) {
                        setupAdministratorMode(it.getOrNull()!!)
                    } else {
                        participantUseCases.getByUidUseCase(
                            br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.GetByUidUseCase.Input(
                                participantToBeRetrievedUid = uid
                            )
                        ).result.let {
                            setupParticipantMode(it.getOrNull())
                        }
                    }
                }
            } else {
                signOut()
            }
        } ?: {
            signOut()
        }
    }

    private fun setupAdministratorMode(administrator: Administrator) {
        _route.value = BaseScreens.InstitutionsScreen.route

        bottomNavigationItens = listOf(
            BottomNavigationItem(
                title = "Instituições",
                route = BaseScreens.InstitutionsScreen.route,
                selectedIcon = Icons.Filled.Apartment,
                unselectedIcon = Icons.Outlined.Apartment,
            ),
            BottomNavigationItem(
                title = "Participantes",
                route = BaseScreens.ParticipantsScreen.route,
                selectedIcon = Icons.Filled.Groups,
                unselectedIcon = Icons.Outlined.Groups,
            ),
            BottomNavigationItem(
                title = "Perfil",
                route = BaseScreens.ProfileScreen.route,
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person
            ),
        )
    }

    private fun setupParticipantMode(participant: Participant?) {
        if (participant != null) {
            if (participant.institution != null) {
                _route.value = BaseScreens.HomeScreen.route
            } else {
                _route.value = BaseScreens.WaitingApproveScreen.route
            }
        } else {
            _route.value = BaseScreens.ProfileScreenWithoutBottomNavBar.route
        }

        bottomNavigationItens = mutableListOf<BottomNavigationItem>().apply {
            add(
                BottomNavigationItem(
                    title = "Início",
                    route = BaseScreens.HomeScreen.route,
                    selectedIcon = Icons.Filled.Home,
                    unselectedIcon = Icons.Outlined.Home,
                )
            )
            add(
                BottomNavigationItem(
                    title = "Perguntas",
                    route = BaseScreens.QuestionsScreen.route,
                    selectedIcon = Icons.Filled.Forum,
                    unselectedIcon = Icons.Outlined.Forum,
                )
            )
            if (participant?.privilege == Privilege.PRINCIPAL) {
                add(
                    BottomNavigationItem(
                        title = "Participantes",
                        route = BaseScreens.ParticipantsScreen.route,
                        selectedIcon = Icons.Filled.Groups,
                        unselectedIcon = Icons.Outlined.Groups,
                    )
                )
            }
            add(
                BottomNavigationItem(
                    title = "Perfil",
                    route = BaseScreens.ProfileScreen.route,
                    selectedIcon = Icons.Filled.Person,
                    unselectedIcon = Icons.Outlined.Person
                )
            )
        }
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        _route.value = BaseScreens.InitialActivity.route
    }
}