package com.farrasmuhammadrazan0100.assement2.model

import com.google.gson.annotations.SerializedName

data class ShiroManhwaResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("Category")
    val category: ShiroCategory
)

data class ShiroCategory(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)