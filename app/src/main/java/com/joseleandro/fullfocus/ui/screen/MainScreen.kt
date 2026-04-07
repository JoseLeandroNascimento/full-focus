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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.joseleandro.fullfocus.core.navigation.ETabScreen
import com.joseleandro.fullfocus.core.navigation.TabScreen
import com.joseleandro.fullfocus.ui.screen.list_tasks.ListTasksScreen
import com.joseleandro.fullfocus.ui.screen.pomodoro.PomodoroScreen
import com.joseleandro.fullfocus.ui.screen.report.ReportScreen
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen() {

    val navigationViewModel: NavigationViewModel = koinViewModel()
    val backStack by navigationViewModel.tabBackStack.collectAsStateWithLifecycle()
    val currentTab = navigationViewModel.currentTab

    MainScreen(
        backStack = backStack,
        onBack = {},
        onNavigate = navigationViewModel::selectedTab,
        currentTab = currentTab
    )
}

@Composable
fun MainScreen(
    backStack: List<TabScreen>,
    onBack: () -> Unit,
    currentTab: TabScreen,
    onNavigate: (TabScreen) -> Unit
) {

    val isPreview = LocalInspectionMode.current

    Scaffold(
        bottomBar = {
            MainBottomAppBar(
                currentTab = currentTab,
                onNavigate = onNavigate
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
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

                        entry<TabScreen.PomodoroScreen> {
                            PomodoroScreen()
                        }

                        entry<TabScreen.ListTasksScreen> {
                            ListTasksScreen()
                        }

                        entry<TabScreen.ReportScreen> {
                            ReportScreen()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MainBottomAppBar(
    modifier: Modifier = Modifier,
    currentTab: TabScreen,
    onNavigate: (TabScreen) -> Unit
) {
    BottomAppBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface
    ) {

        NavigationBar(
            containerColor = Color.Transparent
        ) {
            ETabScreen.entries.forEach { tabScreen ->

                NavigationBarItem(
                    selected = tabScreen.route == currentTab,
                    onClick = { onNavigate(tabScreen.route) },
                    icon = {
                        Icon(
                            painter = painterResource(id = tabScreen.iconRes),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = tabScreen.label),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                )
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
        MainScreen(
            backStack = listOf(TabScreen.PomodoroScreen),
            onBack = {},
            currentTab = TabScreen.PomodoroScreen,
            onNavigate = {}
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
            backStack = listOf(TabScreen.PomodoroScreen),
            onBack = {},
            currentTab = TabScreen.PomodoroScreen,
            onNavigate = {}
        )
    }
}