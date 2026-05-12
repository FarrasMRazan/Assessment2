package com.farrasmuhammadrazan0100.assement2.database
import androidx.room.*
import com.farrasmuhammadrazan0100.assement2.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterDao {
    interface CharacterDao {
        @Insert
        suspend fun insertCharacter(character: Character)

        @Update
        suspend fun updateCharacter(character: Character)

        @Query("SELECT * FROM character_table WHERE manhwaId = :manhwaId")
        fun getCharactersByManhwa(manhwaId: Int): Flow<List<Character>>

        @Delete
        suspend fun deleteCharacter(character: Character)
    }
}