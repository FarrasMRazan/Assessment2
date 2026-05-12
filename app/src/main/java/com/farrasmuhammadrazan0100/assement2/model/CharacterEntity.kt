package com.farrasmuhammadrazan0100.assement2.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "character_table",
    foreignKeys = [
        ForeignKey(
            entity = ManhwaEntity::class,
            parentColumns = ["id"],
            childColumns = ["manhwaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val charId: Int = 0,
    val manhwaId: Int,
    val charName: String,
    val charRole: String
)