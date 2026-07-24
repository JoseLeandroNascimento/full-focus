package com.joseleandro.fullfocus.ui.screen.main.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

@Composable
fun MainBottomNavigationBar(
    modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit
) {
    NavigationBar(
        modifier = modifier, containerColor = Color.Transparent, contentColor = Color.Transparent
    ) {
        content()
    }
}

@Composable
fun RowScope.MainBarItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    @DrawableRes iconRes: Int,
    label: String,
) {
    NavigationBarItem(
        modifier = modifier, colors = NavigationBarItemDefaults.colors(
        ), selected = selected, onClick = onClick, icon = {
            Icon(
                painter = painterResource(id = iconRes), contentDescription = label
            )
        }, label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
                )
            )
        }
    )
}