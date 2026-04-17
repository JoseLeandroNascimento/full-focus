package com.joseleandro.fullfocus.ui.screen.pomodoro.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.domain.data.TaskDomain
import com.joseleandro.fullfocus.ui.screen.list_tasks.component.TaskCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTaskBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    tasks: List<TaskDomain>,
    onDismissRequest: () -> Unit,
    onSelectTask: (Int) -> Unit
) {
    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                text = stringResource(R.string.selecione_uma_tarefa),
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = tasks, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        onClick = { onSelectTask(task.id) }
                    )
                }
            }
        }
    }
}
