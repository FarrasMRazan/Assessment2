package com.farrasmuhammadrazan0100.assement2.network

import com.farrasmuhammadrazan0100.assement2.model.JikanSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface JikanApiService {

    @GET("manga")
    suspend fun searchManhwa(
        @Query("q") query: String,
        @Query("type") type: String = "manhwa",
        @Query("limit") limit: Int = 1
    ): JikanSearchResponse
}