package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.udesc.eso.tcc.studytalk.R

@Composable
fun StudyTalkEnrollmentRequest(
    text: String,
    onApproveClick: () -> Unit,
    onReproveClick: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Row {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1.0f)
            ) {
                Text(text = text)
            }
            IconButton(
                modifier = Modifier
                    .padding(top = 4.dp),
                onClick = { onApproveClick() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(R.string.approve)
                )
            }
            IconButton(
                modifier = Modifier
                    .padding(top = 4.dp),
                onClick = { onReproveClick() }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = stringResource(R.string.reprove)
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}