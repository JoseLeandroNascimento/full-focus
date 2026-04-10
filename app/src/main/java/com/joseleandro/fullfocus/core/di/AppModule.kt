package com.joseleandro.fullfocus.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.room.Room
import com.joseleandro.fullfocus.data.datasource.TagLocalDataSource
import com.joseleandro.fullfocus.data.datasource.TagLocalDataSourceImpl
import com.joseleandro.fullfocus.data.datasource.TaskFilterLocalPreferencesDataSource
import com.joseleandro.fullfocus.data.datasource.TaskFilterLocalPreferencesDataSourceImpl
import com.joseleandro.fullfocus.data.datasource.TaskLocalDataSource
import com.joseleandro.fullfocus.data.datasource.TaskLocalDataSourceImpl
import com.joseleandro.fullfocus.data.local.database.FULL_FOCUS_DATABASE_NAME
import com.joseleandro.fullfocus.data.local.database.FullFocusDatabase
import com.joseleandro.fullfocus.data.local.database.dao.TagDao
import com.joseleandro.fullfocus.data.local.database.dao.TaskDao
import com.joseleandro.fullfocus.data.local.preferences.UserPreferences
import com.joseleandro.fullfocus.data.local.preferences.userPreferencesDataStore
import com.joseleandro.fullfocus.data.repository.TagRepositoryImpl
import com.joseleandro.fullfocus.data.repository.TaskFilterPreferencesRepositoryImpl
import com.joseleandro.fullfocus.data.repository.TaskRepositoryImpl
import com.joseleandro.fullfocus.domain.repository.TagRepository
import com.joseleandro.fullfocus.domain.repository.TaskFilterPreferencesRepository
import com.joseleandro.fullfocus.domain.repository.TaskRepository
import com.joseleandro.fullfocus.domain.usecase.DeleteTagUseCase
import com.joseleandro.fullfocus.domain.usecase.FilterTaskUseCase
import com.joseleandro.fullfocus.domain.usecase.GetArgumentFiltersTasks
import com.joseleandro.fullfocus.domain.usecase.GetFilteredTasksUseCase
import com.joseleandro.fullfocus.domain.usecase.GetTagFindAllUseCase
import com.joseleandro.fullfocus.domain.usecase.GetTagsWithDetailsUseCase
import com.joseleandro.fullfocus.domain.usecase.SaveTagUseCase
import com.joseleandro.fullfocus.domain.usecase.SaveTaskUseCase
import com.joseleandro.fullfocus.ui.screen.NavigationViewModel
import com.joseleandro.fullfocus.ui.screen.create_tag.CreateTagViewModel
import com.joseleandro.fullfocus.ui.screen.create_task.CreateTaskViewModel
import com.joseleandro.fullfocus.ui.screen.list_tasks.ListTasksViewModel
import com.joseleandro.fullfocus.ui.screen.manage_tag.ManageTagViewModel
import com.joseleandro.fullfocus.ui.screen.pomodoro.PomodoroViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object AppModule {

    val dataModule = module {

        single<FullFocusDatabase> {
            Room.databaseBuilder(
                context = androidApplication(),
                klass = FullFocusDatabase::class.java,
                name = FULL_FOCUS_DATABASE_NAME
            ).fallbackToDestructiveMigrationFrom(dropAllTables = true).build()
        }

        single<TagDao> {
            get<FullFocusDatabase>().tagDao()
        }

        single<TaskDao> {
            get<FullFocusDatabase>().taskDao()
        }

        single<TagLocalDataSource> {
            TagLocalDataSourceImpl(
                tagDao = get()
            )
        }

        single<DataStore<UserPreferences>> {
            get<Context>().userPreferencesDataStore
        }

        single<TaskLocalDataSource> {
            TaskLocalDataSourceImpl(
                taskDao = get(),
                taskFilterLocalPreferencesDataSource = get()
            )
        }

        single<TagRepository> {
            TagRepositoryImpl(
                tagLocalDataSource = get()
            )
        }

        single<TaskRepository> {
            TaskRepositoryImpl(
                taskLocalDataSource = get()
            )
        }

        single<TaskFilterLocalPreferencesDataSource> {
            TaskFilterLocalPreferencesDataSourceImpl(
                dataStore = get()
            )
        }

        single<TaskFilterPreferencesRepository> {
            TaskFilterPreferencesRepositoryImpl(
                taskFilterLocalPreferencesDataSource = get()
            )
        }

    }


    val uiDomainModule = module {

        factory {
            SaveTagUseCase(
                tagRepository = get()
            )
        }

        factory {
            GetTagFindAllUseCase(
                tagRepository = get()
            )
        }

        factory {
            SaveTaskUseCase(
                taskRepository = get()
            )
        }

        factory {
            GetFilteredTasksUseCase(
                taskRepository = get()
            )
        }

        factory {
            FilterTaskUseCase(
                taskFilterPreferencesRepository = get()
            )
        }

        factory {
            GetArgumentFiltersTasks(
                taskFilterPreferencesRepository = get()
            )
        }

        factory {
            GetTagsWithDetailsUseCase(
                tagRepository = get()
            )
        }

        factory {
            DeleteTagUseCase(
                tagRepository = get()
            )
        }

    }

    val uiModule = module {

        viewModelOf(::NavigationViewModel)

        viewModelOf(::PomodoroViewModel)

        viewModelOf(::CreateTagViewModel)

        viewModelOf(::ListTasksViewModel)

        viewModelOf(::CreateTaskViewModel)

        viewModelOf(::ManageTagViewModel)
    }

}