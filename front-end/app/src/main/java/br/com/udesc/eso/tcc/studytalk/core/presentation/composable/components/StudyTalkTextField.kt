package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun StudyTalkTextField(
    label: String,
    value: String,
    leadingVector: ImageVector? = null,
    leadingDescription: String? = null,
    singleLine: Boolean,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions = KeyboardActions(),
    onValueChange: (String) -> Unit,
    removeSpacer: Boolean = false
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = label) },
        value = value,
        leadingIcon = leadingVector?.let {
            {
                Icon(
                    imageVector = leadingVector,
                    contentDescription = leadingDescription
                )
            }
        },
        singleLine = singleLine,
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        onValueChange = { newValue -> onValueChange(newValue) }
    )
    if (!removeSpacer) {
        Spacer(modifier = Modifier.height(16.dp))
    }
}