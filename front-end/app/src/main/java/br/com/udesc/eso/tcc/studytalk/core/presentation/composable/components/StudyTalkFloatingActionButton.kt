package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun StudyTalkFloatingActionButton(
    iconVector: ImageVector,
    iconDescription: String,
    text: String? = null,
    onClick: () -> Unit
) {
    if (text != null) {
        ExtendedFloatingActionButton(
            onClick = { onClick() },
            containerColor = MaterialTheme.colorScheme.primary,
            icon = {
                Icon(
                    imageVector = iconVector,
                    contentDescription = iconDescription,
                    tint = MaterialTheme.colorScheme.surface
                )
            },
            text = { Text(text = text) },
        )
    } else {
        FloatingActionButton(
            onClick = { onClick() },
            containerColor = MaterialTheme.colorScheme.primary,
        ) {
            Icon(
                imageVector = iconVector,
                contentDescription = iconDescription,
                tint = MaterialTheme.colorScheme.surface,
            )
        }
    }
}