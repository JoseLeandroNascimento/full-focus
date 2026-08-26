package com.joseleandro.fullfocus.ui.screen.main

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.core.model.Screen
import com.joseleandro.fullfocus.core.model.TabScreen
import com.joseleandro.fullfocus.core.model.index
import com.joseleandro.fullfocus.core.viewModel.FullFocusNavigationViewModel
import com.joseleandro.fullfocus.ui.screen.main.component.MainBarItem
import com.joseleandro.fullfocus.ui.screen.main.component.MainBottomNavigationBar
import com.joseleandro.fullfocus.ui.screen.pomodoro.PomodoroScreen
import com.joseleandro.fullfocus.ui.screen.score.ScoreScreen
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen() {

    val navigationViewModel = koinViewModel<FullFocusNavigationViewModel>()
    val uiState by navigationViewModel.uiState.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    fun openDrawer() {
        scope.launch {
            drawerState.open()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 12.dp)
                ) {


                    NavigationDrawerItem(
                        label = { Text(stringResource(id = R.string.configurar_meta)) },
                        selected = false,
                        icon = { Icon(painterResource(R.drawable.ri_target_fill), null) },
                        onClick = {
                            scope.launch { drawerState.close() }
                            navigationViewModel.navigate(Screen.MetaScreen)
                        },
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    ) {

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
                            PomodoroScreen(
                                openDrawer = {
                                    openDrawer()
                                }
                            )
                        }

                        entry<TabScreen.ScoreTabScreen> {
                            ScoreScreen(
                                openDrawer = {
                                    openDrawer()
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}
