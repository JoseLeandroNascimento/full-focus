package com.joseleandro.fullfocus.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

@Composable
fun FullFocusButtonMainAction(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(
            vertical = 16.dp,
            horizontal = 32.dp
        )
    ) {
        Icon(
            painter = painterResource(
                id = R.drawable.line_md_play_filled,
            ),
            contentDescription = null
        )
        Text(
            text = "Iniciar foco",
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview
@Composable
private fun FullFocusButtonMainActionLightPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        FullFocusButtonMainAction(
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun FullFocusButtonMainActionDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        FullFocusButtonMainAction(
            onClick = {}
        )
    }
}