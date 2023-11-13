package br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.presentation.enrollmentRequesties.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.udesc.eso.tcc.studytalk.R
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkEnrollmentRequest
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.presentation.enrollmentRequesties.viewmodel.EnrollmentRequestiesEvent
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.presentation.enrollmentRequesties.viewmodel.EnrollmentRequestiesViewModel

@Composable
fun EnrollmentRequestiesScreen(
    viewModel: EnrollmentRequestiesViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp, end = 16.dp, start = 16.dp, top = 64.dp)
    ) {
        viewModel.state.value.enrollmentRequesties.let { enrollmentRequesties ->
            if (enrollmentRequesties.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(enrollmentRequesties) { enrollmentRequest ->
                        StudyTalkEnrollmentRequest(
                            text = enrollmentRequest.participant.name,
                            onApproveClick = {
                                viewModel.onEvent(
                                    EnrollmentRequestiesEvent.Approve(
                                        enrollmentRequest.id
                                    )
                                )
                            },
                            onReproveClick = {
                                viewModel.onEvent(
                                    EnrollmentRequestiesEvent.Reprove(
                                        enrollmentRequest.id
                                    )
                                )
                            }
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        imageVector = Icons.Filled.SearchOff,
                        contentDescription = stringResource(R.string.no_results),
                        tint = MaterialTheme.colorScheme.outlineVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.no_results),
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}