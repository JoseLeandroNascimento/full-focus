package com.joseleandro.fullfocus.core.di

import com.joseleandro.fullfocus.ui.screen.NavigationViewModel
import com.joseleandro.fullfocus.ui.screen.pomodoro.PomodoroViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object AppModule {

    val uiModule = module {

        viewModelOf(::NavigationViewModel)

        viewModelOf(::PomodoroViewModel)
    }

}