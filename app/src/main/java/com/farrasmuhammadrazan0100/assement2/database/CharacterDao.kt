package com.farrasmuhammadrazan0100.assement2.database
import androidx.room.*
import com.farrasmuhammadrazan0100.assement2.model.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
        @Insert
        suspend fun insertCharacter(characterEntity: CharacterEntity)

        @Update
        suspend fun updateCharacter(characterEntity: CharacterEntity)

        @Query("SELECT * FROM character_table WHERE manhwaId = :manhwaId")
        fun getCharactersByManhwa(manhwaId: Int): Flow<List<CharacterEntity>>

        @Delete
        suspend fun deleteCharacter(characterEntity: CharacterEntity)
    }