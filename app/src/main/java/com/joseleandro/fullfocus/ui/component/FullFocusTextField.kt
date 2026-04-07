package com.joseleandro.fullfocus.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

@Composable
fun FullFocusTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    maxLines: Int = 1
) {

    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        shape = MaterialTheme.shapes.medium.copy(
            bottomStart = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp)
        ),
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge
            )
        },
        isError = error != null,
        supportingText = error?.let { errorMessage ->
            {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.error
                    )
                )
            }
        },
        maxLines = maxLines,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    )
}

@Preview
@Composable
private fun FullFocusTextFieldLightPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        FullFocusTextField(
            label = stringResource(R.string.nome_da_tag),
            value = "",
            error = "Campo obrigatório",
            onValueChange = {},
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.round_bookmarks_24),
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_palette_24),
                        contentDescription = null
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun FullFocusTextFieldDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        FullFocusTextField(
            label = stringResource(R.string.nome_da_tag),
            value = "",
            onValueChange = {}
        )
    }
}