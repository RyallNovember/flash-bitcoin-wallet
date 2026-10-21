package com.ryall.bitcoinwallet.data.remote.api

import com.ryall.bitcoinwallet.data.remote.dto.FixerFluctuationDto
import com.ryall.bitcoinwallet.data.remote.dto.FixerRatesDto
import retrofit2.http.GET
import retrofit2.http.Query

interface FixerApi {

    @GET("fixer/latest")
    suspend fun getLatestRates(
        @Query("symbols") symbols: String = "BTC,ZAR,USD,AUD"
    ): FixerRatesDto

    @GET("fixer/fluctuation")
    suspend fun getFluctuation(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("symbols") symbols: String = "BTC,ZAR,USD,AUD"
    ): FixerFluctuationDto
}