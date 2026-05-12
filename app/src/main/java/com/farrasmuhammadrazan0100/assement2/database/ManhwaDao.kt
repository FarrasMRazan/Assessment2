package com.farrasmuhammadrazan0100.assement2.database
import androidx.room.*
import com.farrasmuhammadrazan0100.assement2.model.Manhwa
import kotlinx.coroutines.flow.Flow

interface ManhwaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertManhwa(manhwa: Manhwa)

    @Update
    suspend fun updateManhwa(manhwa: Manhwa)

    @Delete
    suspend fun deleteManhwa(manhwa: Manhwa)

    @Query("SELECT * FROM manhwa_table ORDER BY title ASC")
    fun getAllManhwa(): Flow<List<Manhwa>>
}