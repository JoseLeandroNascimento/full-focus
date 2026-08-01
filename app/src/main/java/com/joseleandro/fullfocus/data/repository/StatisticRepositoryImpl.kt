package com.joseleandro.fullfocus.data.repository

import com.joseleandro.fullfocus.data.datasource.StatisticPomodoroDataSource
import com.joseleandro.fullfocus.domain.model.StatisticDomain
import com.joseleandro.fullfocus.domain.repository.StatisticRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class StatisticRepositoryImpl(
    private val statisticDataSource: StatisticPomodoroDataSource
) : StatisticRepository {
    override fun getStatistics(): Flow<StatisticDomain> {
        return statisticDataSource.statistic
    }

    override fun getStatisticsByMonth(date: LocalDate): Flow<StatisticDomain> {
        return statisticDataSource.getStatisticByMonth(date)
    }
}
