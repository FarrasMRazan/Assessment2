package com.farrasmuhammadrazan0100.assement2.model

import com.google.gson.annotations.SerializedName

data class JikanSearchResponse(
    @SerializedName("data")
    val data: List<JikanManga>
)

data class JikanManga(
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("images")
    val images: JikanImages
)

data class JikanImages(
    @SerializedName("jpg")
    val jpg: JikanImageUrls
)

data class JikanImageUrls(
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("large_image_url")
    val largeImageUrl: String
)