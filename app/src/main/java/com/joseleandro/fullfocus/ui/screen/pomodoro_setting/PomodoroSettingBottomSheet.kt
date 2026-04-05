package com.joseleandro.fullfocus.ui.screen.pomodoro_setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.component.FullFocusBottomSheet
import com.joseleandro.fullfocus.ui.component.FullFocusBottomSheetHeader
import com.joseleandro.fullfocus.ui.screen.pomodoro_setting.component.TimerInput
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroSettingBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    sheetState: SheetState
) {

    FullFocusBottomSheet(
        modifier = modifier,
        header = {
            FullFocusBottomSheetHeader(
                title = {
                    Text(
                        text = "Configurar",
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = onDismissRequest
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_close_24),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = .6f
                            )
                        )
                    }
                }
            )
        },
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.95f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp
                )
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.outline_access_time_24),
                    contentDescription = null
                )
                Text(
                    text = "Tempo",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                TimerInput(
                    label = stringResource(R.string.label_pomodoro),
                    modifier = Modifier.weight(1f),
                    value = "25",
                    onValueChange = {}
                )

                TimerInput(
                    label = stringResource(R.string.label_pausa_curta),
                    modifier = Modifier.weight(1f),
                    value = "5",
                    onValueChange = {}
                )

                TimerInput(
                    label = stringResource(R.string.label_pausa_longa),
                    modifier = Modifier.weight(1f),
                    value = "15",
                    onValueChange = {}
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                ListItem(
                    modifier = Modifier.padding(horizontal = 0.dp),
                    headlineContent = {
                        Text(
                            text = "Pausas automáticas",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .7f)
                            )
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = false,
                            onCheckedChange = {}
                        )
                    }
                )

                ListItem(
                    modifier = Modifier.padding(horizontal = 0.dp),
                    headlineContent = {
                        Text(
                            text = "Pomodoros automáticos",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .7f)
                            )
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = true,
                            onCheckedChange = {}
                        )
                    }
                )

                ListItem(
                    modifier = Modifier.padding(horizontal = 0.dp),
                    headlineContent = {
                        Text(
                            text = "Pausa longo a cada",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .7f)
                            )
                        )
                    },
                    trailingContent = {
                        TimerInput(
                            modifier = Modifier.width(100.dp),
                            value = "4",
                            onValueChange = {}
                        )
                    }
                )
            }
        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PomodoroSettingBottomSheetLightPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        PomodoroSettingBottomSheet(
            sheetState = rememberModalBottomSheetState(),
            onDismissRequest = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PomodoroSettingBottomSheetDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        PomodoroSettingBottomSheet(
            sheetState = rememberModalBottomSheetState(),
            onDismissRequest = {}
        )
    }
}