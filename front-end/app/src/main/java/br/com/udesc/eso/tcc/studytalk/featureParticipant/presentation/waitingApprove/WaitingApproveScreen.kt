package br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.waitingApprove

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.create.CreateParticipantViewModel

@Composable
fun WaitingApproveInScreen(
    navController: NavController,
    viewModel: CreateParticipantViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Waiting Approve",
            fontSize = 24.sp,
            color = Color.Black

        )
    }
}