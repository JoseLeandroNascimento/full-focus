package com.joseleandro.fullfocus.ui.screen.progress_time_color_customize

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toColorLong
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.domain.effect.PickerColorEffect
import com.joseleandro.fullfocus.ui.event.PickerColorEvent
import com.joseleandro.fullfocus.ui.screen.progress_time_color_customize.component.PickerColorDialog
import com.joseleandro.fullfocus.ui.screen.progress_time_color_customize.component.PickerColorType
import com.joseleandro.fullfocus.ui.state.PickerColorUiState
import com.joseleandro.fullfocus.ui.theme.ColorStyle
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

private val PRESET_COLORS = listOf(
    // Modern & Vibrant
    Color(0xFFF44336), Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF673AB7),
    Color(0xFF3F51B5), Color(0xFF2196F3), Color(0xFF03A9F4), Color(0xFF00BCD4),
    Color(0xFF009688), Color(0xFF4CAF50), Color(0xFF8BC34A), Color(0xFFCDDC39),
    Color(0xFFFFEB3B), Color(0xFFFFC107), Color(0xFFFF9800), Color(0xFFFF5722),
    // Soft & Pastels
    Color(0xFFFF8A80), Color(0xFFFF80AB), Color(0xFFEA80FC), Color(0xFFB388FF),
    Color(0xFF8C9EFF), Color(0xFF82B1FF), Color(0xFF80D8FF), Color(0xFF84FFFF),
    Color(0xFFA7FFEB), Color(0xFFB9F6CA), Color(0xFFCCFF90), Color(0xFFF4FF81),
    Color(0xFFFFFF8D), Color(0xFFFFE57F), Color(0xFFFFD180), Color(0xFFFF9E80),
    // Deep & Earthy
    Color(0xFFD32F2F), Color(0xFFC2185B), Color(0xFF7B1FA2), Color(0xFF512DA8),
    Color(0xFF303F9F), Color(0xFF1976D2), Color(0xFF0288D1), Color(0xFF0097A7),
    Color(0xFF00796B), Color(0xFF388E3C), Color(0xFF689F38), Color(0xFFAFB42B),
    Color(0xFFFBC02D), Color(0xFFFFA000), Color(0xFFF57C00), Color(0xFFE64A19),
    Color(0xFF795548), Color(0xFF616161), Color(0xFF455A64), Color(0xFF263238)
)

private val PRESET_GRADIENTS = listOf(
    listOf(Color(0xFFFF5F6D), Color(0xFFFFC371)),
    listOf(Color(0xFF2193B0), Color(0xFF6DD5ED)),
    listOf(Color(0xFFEE9CA7), Color(0xFFFFDDE1)),
    listOf(Color(0xFF06BEB6), Color(0xFF48B1BF)),
    listOf(Color(0xFF642B73), Color(0xFFC6426E)),
    listOf(Color(0xFFCB2D3E), Color(0xFFEF473A)),
    listOf(Color(0xFF56AB2F), Color(0xFFA8E063)),
    listOf(Color(0xFF614385), Color(0xFF516395)),
    listOf(Color(0xFF02AAB0), Color(0xFF00CDAC)),
    listOf(Color(0xFF4568DC), Color(0xFFB06AB3)),
    listOf(Color(0xFF43C6AC), Color(0xFFF8FFAE)),
    listOf(Color(0xFFFF9A9E), Color(0xFFFAD0C4)),
    listOf(Color(0xFF88D3CE), Color(0xFF6E45E2)),
    listOf(Color(0xFFD4FC79), Color(0xFF96E6A1)),
    listOf(Color(0xFF84FAB0), Color(0xFF8FD3F4)),
    listOf(Color(0xFFA1C4FD), Color(0xFFC2E9FB)),
    listOf(Color(0xFFFF0844), Color(0xFFFFB199)),
    listOf(Color(0xFFF093FB), Color(0xFFF5576C)),
    listOf(Color(0xFF5EE7DF), Color(0xFFB490CA)),
    listOf(Color(0xFFC31432), Color(0xFF240B36)),
    listOf(Color(0xFF30E8BF), Color(0xFFFF8235)),
    listOf(Color(0xFF0575E6), Color(0xFF021B79))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressTimeColorCustomizeScreen(
    type: PickerColorType,
    initialColor: ColorStyle,
    viewModel: ProgressTimeColorCustomizeViewModel,
    onNavigateBack: () -> Unit,
    onConfirm: (ColorStyle) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.init(type, initialColor)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PickerColorEffect.ConfirmColor -> onConfirm(effect.color)
                PickerColorEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    ProgressTimeColorCustomizeContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProgressTimeColorCustomizeContent(
    uiState: PickerColorUiState,
    onEvent: (PickerColorEvent) -> Unit
) {
    if (uiState.showCustomPicker) {
        PickerColorDialog(
            type = uiState.type,
            color = uiState.selectedColor,
            onConfirm = {
                onEvent(PickerColorEvent.OnColorSelected(it))
                onEvent(PickerColorEvent.OnToggleCustomPicker(false))
            },
            onCancel = { onEvent(PickerColorEvent.OnToggleCustomPicker(false)) },
            onDismissRequest = { onEvent(PickerColorEvent.OnToggleCustomPicker(false)) }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.personalizar_estilo_visual),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(PickerColorEvent.OnCancel) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_arrow_back_ios_new_24),
                            contentDescription = stringResource(R.string.cancelar)
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { onEvent(PickerColorEvent.OnConfirm) }) {
                        Text(text = stringResource(R.string.confirmar))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PresetPickerContent(
                colorSelect = uiState.selectedColor,
                onColorSelect = { onEvent(PickerColorEvent.OnColorSelected(it)) },
                onShowCustom = { onEvent(PickerColorEvent.OnToggleCustomPicker(true)) },
                type = uiState.type,
                selectedTab = uiState.selectedTab,
                onTabChanged = { onEvent(PickerColorEvent.OnTabChanged(it)) }
            )
        }
    }
}

@Composable
private fun PresetPickerContent(
    colorSelect: ColorStyle,
    onColorSelect: (ColorStyle) -> Unit,
    onShowCustom: () -> Unit,
    type: PickerColorType,
    selectedTab: Int,
    onTabChanged: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(
                    R.string.escolha_a_cor_que_melhor_representa_o_periodo_de_1s,
                    stringResource(type.label).lowercase()
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            SecondaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                divider = {},
                indicator = {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(selectedTab),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { onTabChanged(0) },
                    text = {
                        Text(
                            text = stringResource(R.string.estilo_solido),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { onTabChanged(1) },
                    text = {
                        Text(
                            text = stringResource(R.string.estilo_gradiente),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                )
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            if (selectedTab == 0) {
                SolidColorGrid(
                    colorSelect = colorSelect,
                    onColorSelect = onColorSelect,
                    onShowCustom = onShowCustom
                )
            } else {
                GradientColorGrid(
                    colorSelect = colorSelect,
                    onColorSelect = onColorSelect
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        PreviewSection(
            colorSelect = colorSelect,
            type = type
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SolidColorGrid(
    colorSelect: ColorStyle,
    onColorSelect: (ColorStyle) -> Unit,
    onShowCustom: () -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PRESET_COLORS.forEach { color ->
            val isSelected =
                colorSelect is ColorStyle.Solid && colorSelect.getPrimaryColor() == color
            Box(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(
                        width = if (isSelected) 3.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                        shape = CircleShape
                    )
                    .clickable { onColorSelect(ColorStyle.fromColor(color)) }
            )
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant,
                    CircleShape
                )
                .clickable { onShowCustom() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.solar_pallete_2_linear),
                contentDescription = stringResource(R.string.cor_personalizada),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GradientColorGrid(
    colorSelect: ColorStyle,
    onColorSelect: (ColorStyle) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PRESET_GRADIENTS.forEach { colors ->
            val isSelected =
                colorSelect is ColorStyle.Gradient && colorSelect.colorsLong == colors.map { it.toColorLong() }
            Box(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(colors))
                    .border(
                        width = if (isSelected) 3.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                        shape = CircleShape
                    )
                    .clickable { onColorSelect(ColorStyle.fromColors(colors)) }
            )
        }
    }
}

@Composable
private fun PreviewSection(
    colorSelect: ColorStyle,
    type: PickerColorType
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                MaterialTheme.shapes.extraLarge
            )
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(MaterialTheme.shapes.large)
                .background(colorSelect.asBrush())
                .border(
                    2.dp,
                    MaterialTheme.colorScheme.outlineVariant,
                    MaterialTheme.shapes.large
                )
        )
        Column {
            Text(
                text = stringResource(R.string.visualiza_o),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(type.label),
                style = MaterialTheme.typography.headlineMedium,
                color = colorSelect.getPrimaryColor()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressTimeColorCustomizeScreenLightPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        ProgressTimeColorCustomizeContent(
            uiState = PickerColorUiState(
                type = PickerColorType.FOCUS_PICKER_COLOR,
                selectedColor = ColorStyle.fromColor(Color(0xFFF44336)),
                selectedTab = 0
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressTimeColorCustomizeScreenDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        ProgressTimeColorCustomizeContent(
            uiState = PickerColorUiState(
                type = PickerColorType.FOCUS_PICKER_COLOR,
                selectedColor = ColorStyle.fromColor(Color(0xFFF44336)),
                selectedTab = 0
            ),
            onEvent = {}
        )
    }
}
