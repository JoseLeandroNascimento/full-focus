package com.joseleandro.fullfocus.ui.screen.manage_tag.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.domain.data.TagWithTasksDetailsDomain

@Composable
fun TagCard(
    modifier: Modifier = Modifier,
    tag: TagWithTasksDetailsDomain,
    onEdit: (TagWithTasksDetailsDomain) -> Unit,
    onDelete: (TagWithTasksDetailsDomain) -> Unit
) {

    var expandedDropdown by remember { mutableStateOf(false) }

    ListItem(
        modifier = modifier,
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.round_bookmarks_24),
                contentDescription = null
            )
        },
        headlineContent = {
            Text(
                text = tag.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
        },
        supportingContent = {
            Text(
                text = "${tag.countTasks} tarefas"
            )
        },
        trailingContent = {

            Box(

            ) {
                IconButton(
                    onClick = {
                        expandedDropdown = true
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_more_vert_24),
                        contentDescription = null
                    )
                }

                DropdownMenu(
                    expanded = expandedDropdown,
                    onDismissRequest = {
                        expandedDropdown = false
                    },
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = 6.dp,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_edit_24),
                                contentDescription = null
                            )
                        },
                        text = {
                            Text(
                                text = stringResource(R.string.editar)
                            )
                        },
                        onClick = {
                            onEdit(tag)
                            expandedDropdown = false
                        }
                    )

                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_delete_24),
                                contentDescription = null
                            )
                        },
                        text = {
                            Text(
                                text = stringResource(R.string.excluir)
                            )
                        },
                        onClick = {
                            onDelete(tag)
                            expandedDropdown = false
                        }
                    )
                }
            }
        }
    )
}