package com.joseleandro.fullfocus.core.di

import androidx.room.Room
import com.joseleandro.fullfocus.core.viewModel.FullFocusNavigation
import com.joseleandro.fullfocus.data.datasource.PomodoroSettingDataSource
import com.joseleandro.fullfocus.data.datasource.PomodoroSettingDataSourceImpl
import com.joseleandro.fullfocus.data.datasource.PomodoroTimerDataSource
import com.joseleandro.fullfocus.data.datasource.PomodoroTimerDataSourceImpl
import com.joseleandro.fullfocus.data.local.database.FULL_FOCUS_DATABASE
import com.joseleandro.fullfocus.data.local.database.FullFocusDataBase
import com.joseleandro.fullfocus.data.local.database.dao.PomodoroDao
import com.joseleandro.fullfocus.data.local.database.dao.SessionDao
import com.joseleandro.fullfocus.data.repository.PomodoroSettingRepositoryImpl
import com.joseleandro.fullfocus.data.repository.PomodoroTimerRepositoryImpl
import com.joseleandro.fullfocus.domain.repository.PomodoroSettingRepository
import com.joseleandro.fullfocus.domain.repository.PomodoroTimerRepository
import com.joseleandro.fullfocus.ui.screen.pomodoro.PomodoroViewModel
import com.joseleandro.fullfocus.ui.screen.pomodoro_setting.PomodoroSettingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object KoinModule {

    val dataModule = module {

        single<FullFocusDataBase> {
            Room.databaseBuilder<FullFocusDataBase>(
                context = androidContext().applicationContext, name = FULL_FOCUS_DATABASE
            ).fallbackToDestructiveMigrationFrom(dropAllTables = true).build()
        }

        single<PomodoroDao> {
            get<FullFocusDataBase>().pomodoroDao()
        }

        single<SessionDao> {
            get<FullFocusDataBase>().sessionDao()
        }

        single<PomodoroTimerDataSource> {
            PomodoroTimerDataSourceImpl(
                context = androidContext().applicationContext
            )
        }

        single<PomodoroTimerRepository> {
            PomodoroTimerRepositoryImpl(
                pomodoroTimerDataSource = get()
            )
        }

        single<PomodoroSettingDataSource> {
            PomodoroSettingDataSourceImpl(
                context = androidContext().applicationContext
            )
        }

        single<PomodoroSettingRepository> {
            PomodoroSettingRepositoryImpl(
                pomodoroSettingDataSource = get()
            )
        }

    }

    val uiModule = module {

        viewModelOf(::FullFocusNavigation)

        viewModelOf(::PomodoroViewModel)

        viewModelOf(::PomodoroSettingViewModel)
    }
}
