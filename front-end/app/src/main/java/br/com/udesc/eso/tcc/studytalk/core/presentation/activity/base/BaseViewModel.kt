package br.com.udesc.eso.tcc.studytalk.core.presentation.activity.base

import android.content.SharedPreferences
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.BottomNavigationItem
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.model.Administrator
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.useCase.AdministratorUseCases
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.model.Participant
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.ParticipantUseCases
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(
    private val administratorUseCases: AdministratorUseCases,
    private val participantUseCases: ParticipantUseCases,
    private val sharedPreferences: SharedPreferences
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
        val currentUid = sharedPreferences.getString("current_uid", "")

        currentUid?.let { uid ->
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
        saveAdministratorToSharedPreferences(administrator)

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
        saveParticipantToSharedPreferences(participant)

        if (participant != null) {
            if (participant.institution != null) {
                _route.value = BaseScreens.HomeScreen.route
            } else {
                _route.value = BaseScreens.WaitingApproveScreen.route
            }
        } else {
            _route.value = BaseScreens.CreateParticipantScreen.route
        }

        bottomNavigationItens = listOf(
            BottomNavigationItem(
                title = "Início",
                route = BaseScreens.HomeScreen.route,
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
            ),
            BottomNavigationItem(
                title = "Perguntas",
                route = BaseScreens.QuestionsScreen.route,
                selectedIcon = Icons.Filled.Forum,
                unselectedIcon = Icons.Outlined.Forum,
            ),
            BottomNavigationItem(
                title = "Perfil",
                route = BaseScreens.ProfileScreen.route,
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person
            ),
        )
    }

    private fun saveAdministratorToSharedPreferences(administrator: Administrator) {
        sharedPreferences.edit().putString("current_mode", "administrator").apply()
        val gson = Gson()
        val administratorString = gson.toJson(administrator)
        sharedPreferences.edit().putString("current_administrator", administratorString).apply()
    }

    private fun saveParticipantToSharedPreferences(participant: Participant?) {
        sharedPreferences.edit().putString("current_mode", "participant").apply()
        if (participant != null) {
            val gson = Gson()
            val participantString = gson.toJson(participant)
            sharedPreferences.edit().putString("current_participant", participantString).apply()
        }
    }

    fun signOut() {
        sharedPreferences.edit().putString("current_uid", "").apply()
        FirebaseAuth.getInstance().signOut()
    }
}