package com.farrasmuhammadrazan0100.assement2.database

import androidx.room.*
import com.farrasmuhammadrazan0100.assement2.model.ManhwaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ManhwaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertManhwa(manhwaEntity: ManhwaEntity)

    @Update
    suspend fun updateManhwa(manhwaEntity: ManhwaEntity)

    @Delete
    suspend fun deleteManhwa(manhwaEntity: ManhwaEntity)


    @Query("SELECT * FROM manhwa_table WHERE userId = '' OR userId = :userId ORDER BY title ASC")
    fun getManhwaByUser(userId: String): Flow<List<ManhwaEntity>>

    @Query("SELECT * FROM manhwa_table ORDER BY title ASC")
    fun getAllManhwa(): Flow<List<ManhwaEntity>>
}