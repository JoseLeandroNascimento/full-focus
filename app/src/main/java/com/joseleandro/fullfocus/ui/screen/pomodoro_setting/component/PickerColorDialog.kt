package com.joseleandro.fullfocus.ui.screen.pomodoro_setting.component

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.joseleandro.fullfocus.R

enum class PickerColorType(@get:StringRes val label: Int) {
    FOCUS_PICKER_COLOR(label = R.string.picker_color_foco),
    SHORT_BREAK_PICKER_COLOR(label = R.string.picker_color_pausa_curta),
    LONG_BREAK_PICKER_COLOR(label = R.string.picker_color_pausa_longa)
}

@Composable
fun PickerColorDialog(
    type: PickerColorType,
    color: Color,
    onDismissRequest: () -> Unit,
    onConfirm: (color: Color) -> Unit,
    onCancel: () -> Unit
) {

    val controller = rememberColorPickerController()
    var colorSelect by remember { mutableStateOf(color) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(colorSelect)
                }
            ) {
                Text(
                    text = stringResource(R.string.confirmar)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel
            ) {
                Text(
                    text = stringResource(R.string.cancelar)
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.cor_da_etapa),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(state = rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(
                        R.string.defina_a_cor_que_representar_o_per_odo_de_na_interface,
                        stringResource(type.label).lowercase()
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AlphaTile(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outlineVariant,
                                MaterialTheme.shapes.medium
                            ),
                        controller = controller
                    )
                    Column {
                        Text(
                            text = stringResource(R.string.visualiza_o),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = stringResource(type.label),
                            style = MaterialTheme.typography.titleMedium,
                            color = colorSelect
                        )
                    }
                }

                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    controller = controller,
                    initialColor = color,
                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                        colorSelect = colorEnvelope.color
                    }
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = stringResource(R.string.brilho),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    BrightnessSlider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(28.dp),
                        controller = controller,
                    )
                }
            }
        }
    )
}