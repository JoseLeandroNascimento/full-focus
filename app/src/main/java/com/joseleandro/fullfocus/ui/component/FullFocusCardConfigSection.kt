package com.joseleandro.fullfocus.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.theme.ColorStyle
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

class FullFocusCardConfigSectionScope {

    internal val options = mutableListOf<@Composable FullFocusCardConfigSectionScope.() -> Unit>()

    fun option(content: @Composable FullFocusCardConfigSectionScope.() -> Unit) {
        options.add(content)
    }
}

@Composable
fun FullFocusCardConfigSection(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleIcon: (@Composable () -> Unit)? = null,
    content: FullFocusCardConfigSectionScope.() -> Unit
) {

    val scope = remember {
        FullFocusCardConfigSectionScope()
    }.apply {
        options.clear()
    }

    scope.content()

    Column(modifier = modifier.fillMaxWidth()) {
        if (title != null || titleIcon != null) {
            Row(
                modifier = Modifier.padding(start = 4.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                titleIcon?.invoke()
                if (title != null) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(fontSize = 14.sp),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
            )
        ) {
            Column {
                scope.options.forEachIndexed { index, option ->
                    scope.option()

                    if (index != scope.options.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ConfigOptionBase(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val alpha = if (enabled) 1f else 0.5f

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null && enabled) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(vertical = 14.dp, horizontal = 16.dp)
            .graphicsLayer {
                this.alpha = alpha
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        leadingContent?.let {
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                it()
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        trailingContent?.invoke()
    }
}

@Composable
fun FullFocusCardConfigSectionScope.ConfigOptionSwitch(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    @DrawableRes icon: Int,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    ConfigOptionBase(
        modifier = modifier,
        onClick = { onCheckedChange(!checked) },
        enabled = enabled,
        leadingContent = {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        title = title,
        subtitle = subtitle,
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.onSurface,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                )

            )
        }
    )
}

@Composable
fun FullFocusCardConfigSectionScope.ConfigOptionColor(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    @DrawableRes icon: Int? = null,
    color: ColorStyle,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    ConfigOptionBase(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        title = title,
        subtitle = subtitle,
        leadingContent = icon?.let {
            {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = it),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        trailingContent = {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(color.asBrush())
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
            )
        }
    )
}

@Composable
fun FullFocusCardConfigSectionScope.ConfigOptionNav(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    @DrawableRes icon: Int? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    ConfigOptionBase(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        leadingContent = icon?.let { iconRes ->
            {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        title = title,
        subtitle = subtitle,
        trailingContent = {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = R.drawable.mingcute_right_line),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    )
}

@Composable
fun FullFocusCardConfigSectionScope.ConfigOption(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    @DrawableRes icon: Int? = null,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null
) {
    ConfigOptionBase(
        modifier = modifier,
        title = title,
        subtitle = subtitle,
        enabled = enabled,
        onClick = onClick,
        leadingContent = icon?.let {
            {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = it),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        trailingContent = trailingContent
    )
}

@Preview(showBackground = true)
@Composable
private fun FullFocusCardConfigSectionLightPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        Column(Modifier.padding(16.dp)) {
            FullFocusCardConfigSection(
                title = "Configurações",
                titleIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.material_symbols_timer_outline_rounded),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                option {
                    ConfigOptionSwitch(
                        title = "Modo silencioso",
                        subtitle = "Silencia todos os alertas",
                        icon = R.drawable.mingcute_volume_mute_line,
                        checked = false,
                        onCheckedChange = {}
                    )
                }
                option {
                    ConfigOptionColor(
                        title = "Cor de foco",
                        subtitle = "Escolha sua cor favorita",
                        color = ColorStyle.fromColor(MaterialTheme.colorScheme.primary),
                        onClick = {}
                    )
                }
                option {
                    ConfigOptionNav(
                        title = "Configurações avançadas",
                        icon = R.drawable.mingcute_volume_mute_line,
                        onClick = {}
                    )
                }
            }
        }
    }
}
