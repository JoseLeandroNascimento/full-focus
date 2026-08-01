package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.domain.model.StatisticDomain
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface StatisticPomodoroDataSource {

    val statistic: Flow<StatisticDomain>

    fun getStatisticByMonth(date: LocalDate): Flow<StatisticDomain>

}
