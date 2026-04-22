package com.joseleandro.fullfocus.ui.screen.pomodoro.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.domain.data.TaskDomain
import com.joseleandro.fullfocus.ui.component.FullFocusBottomSheet
import com.joseleandro.fullfocus.ui.component.FullFocusBottomSheetHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTaskBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    value: TaskDomain?,
    tasks: List<TaskDomain>,
    onDismissRequest: () -> Unit,
    onSelectTask: (Int) -> Unit
) {

    FullFocusBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        header = {
            FullFocusBottomSheetHeader(
                title = {
                    Text(
                        text = stringResource(R.string.selecione_uma_tarefa)
                    )
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.7f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = tasks, key = { it.id }) { task ->

                TaskCardItem(
                    task = task,
                    selected = value?.let { value -> value.id == task.id } ?: false,
                    onClick = { onSelectTask(task.id) }
                )
            }
        }
    }
}

@Composable
private fun TaskCardItem(
    modifier: Modifier = Modifier,
    task: TaskDomain,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .4f),
        shape = MaterialTheme.shapes.large,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = .1f),
                        shape = MaterialTheme.shapes.large
                    )
                    .padding(12.dp),
                painter = painterResource(id = R.drawable.boxicons_seal_check),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )

            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                text = task.title,
                style = MaterialTheme.typography.titleSmall
            )

            RadioButton(
                selected = selected,
                onClick = onClick
            )

        }
    }
}
