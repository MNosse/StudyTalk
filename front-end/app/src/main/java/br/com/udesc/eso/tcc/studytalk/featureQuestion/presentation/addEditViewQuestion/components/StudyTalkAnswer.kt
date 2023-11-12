package br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.addEditViewQuestion.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.udesc.eso.tcc.studytalk.R

@Composable
fun StudyTalkAnswer(
    text: String,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    isOwner: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onReportClick: () -> Unit
) {
    val moreMenuExpanded = remember { mutableStateOf(false) }

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
                onClick = { onLikeClick() }
            ) {
                Icon(
                    imageVector = if (isLiked) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Filled.FavoriteBorder
                    },
                    contentDescription = stringResource(
                        R.string.like
                    )
                )
            }
            Box {
                IconButton(
                    modifier = Modifier
                        .padding(top = 4.dp),
                    onClick = { moreMenuExpanded.value = true }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = stringResource(
                            R.string.more_menu
                        )
                    )
                }

                DropdownMenu(
                    expanded = moreMenuExpanded.value,
                    onDismissRequest = { moreMenuExpanded.value = false }
                ) {
                    if (isOwner) {
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = stringResource(R.string.edit)
                                )
                            },
                            text = { Text(text = stringResource(R.string.edit)) },
                            onClick = {
                                moreMenuExpanded.value = false
                                onEditClick()
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = stringResource(R.string.exclude)
                                )
                            },
                            text = { Text(text = stringResource(R.string.exclude)) },
                            onClick = {
                                moreMenuExpanded.value = false
                                onDeleteClick()
                            }
                        )
                    }
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Report,
                                contentDescription = stringResource(R.string.report)
                            )
                        },
                        text = { Text(text = stringResource(R.string.report)) },
                        onClick = {
                            moreMenuExpanded.value = false
                            onReportClick()
                        }
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}