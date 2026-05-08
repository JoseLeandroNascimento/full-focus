package com.joseleandro.fullfocus.core.di

import com.joseleandro.fullfocus.core.viewModel.FullFocusNavigation
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object KoinModule {

    val uiModule = module {
        viewModelOf(::FullFocusNavigation)
    }
}