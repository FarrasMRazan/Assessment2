package com.farrasmuhammadrazan0100.assement2.network

import com.farrasmuhammadrazan0100.assement2.model.OpStatus
import com.farrasmuhammadrazan0100.assement2.model.ShiroManhwaResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ManhwaApiService {

    @GET("manhwas")
    suspend fun getRandomManhwa(): ShiroManhwaResponse

    @GET("manhwas")
    suspend fun getManhwaByCategory(
        @Query("category") category: String
    ): ShiroManhwaResponse

    @Multipart
    @POST("manhwa.php")
    suspend fun postManhwa(
        @Header("Authorization") userId: String,
        @Part("judul") judul: RequestBody,
        @Part("author") author: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus
}