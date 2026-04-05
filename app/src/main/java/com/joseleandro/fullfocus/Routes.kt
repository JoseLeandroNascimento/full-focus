package com.joseleandro.fullfocus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.joseleandro.fullfocus.core.navigation.Screen
import com.joseleandro.fullfocus.ui.screen.MainScreen
import com.joseleandro.fullfocus.ui.screen.NavigationViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Routes(modifier: Modifier = Modifier) {

    val navigationViewModel: NavigationViewModel = koinViewModel()
    val backStack by navigationViewModel.backStack.collectAsStateWithLifecycle()

    NavDisplay(
        backStack = backStack,
        onBack = navigationViewModel::onBack,
        entryProvider = entryProvider {

            entry<Screen.MainScreen> {
                MainScreen()
            }
        }
    )
}
