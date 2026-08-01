package com.joseleandro.fullfocus.core.di

import androidx.room.Room
import com.joseleandro.fullfocus.core.util.BackgroundSoundPlayer
import com.joseleandro.fullfocus.core.util.VibrationHelper
import com.joseleandro.fullfocus.core.util.VibrationHelperImpl
import com.joseleandro.fullfocus.core.viewModel.FullFocusNavigationViewModel
import com.joseleandro.fullfocus.data.datasource.PomodoroDataSource
import com.joseleandro.fullfocus.data.datasource.PomodoroDataSourceImpl
import com.joseleandro.fullfocus.data.datasource.PomodoroSettingDataSource
import com.joseleandro.fullfocus.data.datasource.PomodoroSettingDataSourceImpl
import com.joseleandro.fullfocus.data.local.database.FULL_FOCUS_DATABASE
import com.joseleandro.fullfocus.data.local.database.FullFocusDataBase
import com.joseleandro.fullfocus.data.local.database.dao.PomodoroDao
import com.joseleandro.fullfocus.data.local.database.dao.SessionDao
import com.joseleandro.fullfocus.data.repository.PomodoroRepositoryImpl
import com.joseleandro.fullfocus.data.repository.PomodoroSettingRepositoryImpl
import com.joseleandro.fullfocus.data.repository.StatisticRepositoryImpl
import com.joseleandro.fullfocus.domain.repository.PomodoroRepository
import com.joseleandro.fullfocus.domain.repository.PomodoroSettingRepository
import com.joseleandro.fullfocus.domain.repository.StatisticRepository
import com.joseleandro.fullfocus.domain.usecase.GetStatisticsUseCase
import com.joseleandro.fullfocus.ui.screen.config_sound.ConfigSoundViewModel
import com.joseleandro.fullfocus.ui.screen.notification_setting.NotificationSettingViewModel
import com.joseleandro.fullfocus.ui.screen.pomodoro.PomodoroViewModel
import com.joseleandro.fullfocus.ui.screen.pomodoro_setting.PomodoroSettingViewModel
import com.joseleandro.fullfocus.ui.screen.progress_time_color_customize.ProgressTimeColorCustomizeViewModel
import com.joseleandro.fullfocus.ui.screen.score.ScoreViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object KoinModule {

    val dataModule = module {

        single<FullFocusDataBase> {
            Room.databaseBuilder<FullFocusDataBase>(
                context = androidContext().applicationContext,
                name = FULL_FOCUS_DATABASE,
            ).fallbackToDestructiveMigration(dropAllTables = true).build()
        }

        single<PomodoroDao> {
            get<FullFocusDataBase>().pomodoroDao()
        }

        single<SessionDao> {
            get<FullFocusDataBase>().sessionDao()
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

        single<PomodoroRepository> {
            PomodoroRepositoryImpl(
                pomodoroSettingDataSource = get(),
                pomodoroDataSource = get()
            )
        }

        single<StatisticRepository> {
            StatisticRepositoryImpl(
                pomodoroDao = get()
            )
        }

        single {
            GetStatisticsUseCase(
                repository = get()
            )
        }

        single<PomodoroDataSource> {
            PomodoroDataSourceImpl(
                pomodoroDao = get(),
                sessionDao = get()
            )
        }

        single<VibrationHelper> {
            VibrationHelperImpl(
                context = androidContext()
            )
        }

        single {
            BackgroundSoundPlayer(
                context = androidContext()
            )
        }

    }

    val uiModule = module {

        viewModelOf(::FullFocusNavigationViewModel)

        viewModelOf(::PomodoroViewModel)

        viewModelOf(::PomodoroSettingViewModel)

        viewModelOf(::ConfigSoundViewModel)

        viewModelOf(::ProgressTimeColorCustomizeViewModel)

        viewModelOf(::NotificationSettingViewModel)

        viewModelOf(::ScoreViewModel)

    }
}
