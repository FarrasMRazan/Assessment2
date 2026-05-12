package com.farrasmuhammadrazan0100.assement2.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "manhwa_table")

    data class ManhwaEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val title: String,
        val author: String,
        val rating: Float,
        val imageUri: String
    )
