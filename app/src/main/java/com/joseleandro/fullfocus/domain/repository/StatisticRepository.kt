package com.joseleandro.fullfocus.domain.repository

import com.joseleandro.fullfocus.domain.model.StatisticDomain
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface StatisticRepository {
    fun getStatistics(): Flow<StatisticDomain>
    fun getStatisticsByMonth(date: LocalDate): Flow<StatisticDomain>
}
