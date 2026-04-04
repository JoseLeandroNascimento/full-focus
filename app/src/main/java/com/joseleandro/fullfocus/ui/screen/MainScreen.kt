package com.joseleandro.fullfocus.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.core.navigation.Screen
import com.joseleandro.fullfocus.ui.screen.pomodoro.PomodoroScreen
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen() {

    val navigationViewModel: NavigationViewModel = koinViewModel()
    val backStack by navigationViewModel.backStack.collectAsStateWithLifecycle()

    MainScreen(
        backStack = backStack,
        onBack = navigationViewModel::onBack
    )
}

@Composable
fun MainScreen(
    backStack: List<Screen>,
    onBack: () -> Unit
) {

    val isPreview = LocalInspectionMode.current

    Scaffold(
        bottomBar = {
            MainBottomAppBar()
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {

            if (!isPreview) {
                NavDisplay(
                    backStack = backStack,
                    onBack = onBack,
                    entryProvider = entryProvider {

                        entry<Screen.PomodoroScreen> {
                            PomodoroScreen()
                        }

                    }
                )
            }
        }
    }
}

@Composable
fun MainBottomAppBar(modifier: Modifier = Modifier) {
    BottomAppBar() {
        NavigationBar() {

            NavigationBarItem(
                selected = true,
                onClick = { /*TODO*/ },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_access_time_filled_24),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = "Pomodoro",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            )

            NavigationBarItem(
                selected = false,
                onClick = { /*TODO*/ },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.round_check_circle_outline_24),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = "Tarefas",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            )

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
        MainScreen(
            backStack = listOf(Screen.PomodoroScreen),
            onBack = {}
        )
    }
}

@Preview
@Composable
private fun MainScreenDarkPreview() {
    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        MainScreen(
            backStack = listOf(Screen.PomodoroScreen),
            onBack = {}
        )
    }
}