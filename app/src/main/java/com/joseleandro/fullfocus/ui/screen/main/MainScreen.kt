package com.joseleandro.fullfocus.ui.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.core.model.TabScreen
import com.joseleandro.fullfocus.core.viewModel.FullFocusNavigationViewModel
import com.joseleandro.fullfocus.ui.screen.pomodoro.PomodoroScreen
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen() {

    val navigationViewModel = koinViewModel<FullFocusNavigationViewModel>()
    val uiState by navigationViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.Transparent,
                contentColor = Color.Transparent
            ) {
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                    ),
                    selected = uiState.tabSelected == TabScreen.PomodoroTabScreen,
                    onClick = { navigationViewModel.selectTab(TabScreen.PomodoroTabScreen) },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.boxicons_timer),
                            contentDescription = stringResource(R.string.icon_tab_pomodoro)
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.pomodoro)
                        )
                    }
                )
                NavigationBarItem(
                    selected = uiState.tabSelected == TabScreen.ScoreTabScreen,
                    onClick = { navigationViewModel.selectTab(TabScreen.ScoreTabScreen) },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.mage_chart),
                            contentDescription = stringResource(R.string.icon_tab_pomodoro)
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.score)
                        )
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .fillMaxSize()
        ) {

            when (uiState.tabSelected) {
                TabScreen.PomodoroTabScreen -> {
                    PomodoroScreen()
                }

                TabScreen.ScoreTabScreen -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Em breve: Tela de Score")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MainScreenLightPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        MainScreen()
    }
}

@Preview
@Composable
private fun MainScreenDarkPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        MainScreen()
    }
}