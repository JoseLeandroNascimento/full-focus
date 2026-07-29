package com.joseleandro.fullfocus.ui.screen.main

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.joseleandro.fullfocus.core.model.TabScreen
import com.joseleandro.fullfocus.core.model.index
import com.joseleandro.fullfocus.core.viewModel.FullFocusNavigationViewModel
import com.joseleandro.fullfocus.ui.screen.main.component.MainBarItem
import com.joseleandro.fullfocus.ui.screen.main.component.MainBottomNavigationBar
import com.joseleandro.fullfocus.ui.screen.pomodoro.PomodoroScreen
import com.joseleandro.fullfocus.ui.screen.score.ScoreScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen() {

    val navigationViewModel = koinViewModel<FullFocusNavigationViewModel>()
    val uiState by navigationViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            MainBottomNavigationBar() {
                TabScreen.items.forEach { tab ->
                    MainBarItem(
                        label = stringResource(id = tab.labelRes),
                        iconRes = tab.iconRes,
                        selected = uiState.tabSelected == tab.route,
                        onClick = {
                            navigationViewModel.selectTab(tab.route)
                        }
                    )
                }
            }
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .fillMaxSize()
        ) {

            NavDisplay(
                backStack = uiState.tabStack,
                onBack = navigationViewModel::onBack,
                transitionSpec = {
                    val initialTab = initialState as? TabScreen
                    val targetTab = targetState as? TabScreen

                    val isForward = if (initialTab != null && targetTab != null) {
                        targetTab.index > initialTab.index
                    } else {
                        true
                    }

                    if (isForward) {
                        slideInHorizontally(initialOffsetX = { -it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { it })
                    } else {
                        slideInHorizontally(initialOffsetX = { it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { -it })
                    }
                },
                popTransitionSpec = {
                    val initialTab = initialState as? TabScreen
                    val targetTab = targetState as? TabScreen

                    val isForward = if (initialTab != null && targetTab != null) {
                        targetTab.index > initialTab.index
                    } else {
                        false
                    }

                    if (isForward) {
                        slideInHorizontally(initialOffsetX = { -it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { it })
                    } else {
                        slideInHorizontally(initialOffsetX = { it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { -it })
                    }
                },
                predictivePopTransitionSpec = {
                    val initialTab = initialState as? TabScreen
                    val targetTab = targetState as? TabScreen

                    val isForward = if (initialTab != null && targetTab != null) {
                        targetTab.index > initialTab.index
                    } else {
                        false
                    }

                    if (isForward) {
                        slideInHorizontally(initialOffsetX = { -it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { it })
                    } else {
                        slideInHorizontally(initialOffsetX = { it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { -it })
                    }
                },
                entryProvider = entryProvider {

                    entry<TabScreen.PomodoroTabScreen> {
                        PomodoroScreen()
                    }

                    entry<TabScreen.ScoreTabScreen> {
                        ScoreScreen()
                    }
                }
            )
        }
    }
}
