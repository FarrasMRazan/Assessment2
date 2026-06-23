package com.farrasmuhammadrazan0100.assement2.model

import com.google.gson.annotations.SerializedName

data class OpStatus(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String?
)