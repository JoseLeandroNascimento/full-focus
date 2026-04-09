package com.joseleandro.fullfocus.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

@Composable
fun FullFocusTagFilterChip(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    leadingIcon: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {

    FilterChip(
        modifier = modifier,
        selected = selected,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
        ),
        leadingIcon = leadingIcon,
        border = BorderStroke(
            width = 2.dp,
            color = if (!selected) MaterialTheme.colorScheme.outlineVariant.copy(alpha = .2f) else Color.Unspecified
        ),
        shape = MaterialTheme.shapes.extraLarge,
        onClick = onClick,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium
                )
            )
        }
    )
}

@Preview
@Composable
private fun FullFocusTagFilterChipLightPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        FullFocusTagFilterChip(
            label = "Trabalho",
            selected = false,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun FullFocusTagFilterChipDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        FullFocusTagFilterChip(
            label = "Trabalho",
            selected = true,
            onClick = {}
        )
    }
}