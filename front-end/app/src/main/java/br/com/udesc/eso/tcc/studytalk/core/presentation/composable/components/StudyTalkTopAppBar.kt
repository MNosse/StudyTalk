package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import br.com.udesc.eso.tcc.studytalk.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyTalkTopAppBar(
    title: String,
    hasNavBack: Boolean = false,
    navBackAction: () -> Unit = {},
    actions: StudyTalkTopAppBarActions? = null
) {
    TopAppBar(
        title = { Text(text = title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.surface
        ),
        navigationIcon = {
            if (hasNavBack) {
                IconButton(onClick = { navBackAction() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.top_app_bar_nav_back),
                        tint = MaterialTheme.colorScheme.surface
                    )
                }
            }
        },
        actions = {
            actions?.let { actions ->
                IconButton(onClick = { actions.onClick(!actions.expanded) }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = stringResource(R.string.more_menu),
                        tint = MaterialTheme.colorScheme.surface
                    )
                }
                DropdownMenu(
                    expanded = actions.expanded,
                    onDismissRequest = { actions.onClick(false) }
                ) {
                    actions.dropdownMenuItens.forEach { item ->
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    imageVector = item.leadingIcon,
                                    contentDescription = item.leadingDescription
                                )
                            },
                            text = { Text(text = item.text) },
                            onClick = {
                                item.onClick()
                                actions.onClick(false)
                            }
                        )
                    }
                }
            }
        }
    )
}

data class StudyTalkTopAppBarActions(
    val expanded: Boolean,
    val onClick: (Boolean) -> Unit,
    val dropdownMenuItens: List<StudyTalkTopAppBarDropdownMenuItem>
)

data class StudyTalkTopAppBarDropdownMenuItem(
    val leadingIcon: ImageVector,
    val leadingDescription: String,
    val text: String,
    val onClick: () -> Unit
)