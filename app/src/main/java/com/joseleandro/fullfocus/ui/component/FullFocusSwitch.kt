package com.joseleandro.fullfocus.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

@Composable
fun FullFocusSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Switch(
        modifier = modifier,
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            uncheckedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .1f),
            uncheckedTrackColor = MaterialTheme.colorScheme.surface,
            uncheckedThumbColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .5f)
        )
    )
}

@Preview
@Composable
private fun FullFocusSwitchLightCheckPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        FullFocusSwitch(
            checked = true,
            onCheckedChange = {}
        )
    }
}

@Preview
@Composable
private fun FullFocusSwitchDarkCheckPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        FullFocusSwitch(
            checked = true,
            onCheckedChange = {}
        )
    }
}

@Preview
@Composable
private fun FullFocusSwitchLightUncheckedPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        FullFocusSwitch(
            checked = false,
            onCheckedChange = {}
        )
    }
}

@Preview
@Composable
private fun FullFocusSwitchDarkUncheckedPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        FullFocusSwitch(
            checked = false,
            onCheckedChange = {}
        )
    }
}