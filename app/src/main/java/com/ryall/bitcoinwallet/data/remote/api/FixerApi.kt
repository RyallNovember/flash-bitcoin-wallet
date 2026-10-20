package com.ryall.bitcoinwallet.data.remote.api

import com.ryall.bitcoinwallet.data.remote.dto.FixerRatesDto
import retrofit2.http.GET
import retrofit2.http.Query

interface FixerApi {

    @GET("fixer/latest")
    suspend fun getLatestRates(
        @Query("symbols") symbols: String = "BTC,ZAR,USD,AUD"
    ): FixerRatesDto
}