package com.farrasmuhammadrazan0100.assement2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.farrasmuhammadrazan0100.assement2.model.ManhwaEntity

@Database(entities = [ManhwaEntity::class], version = 1, exportSchema = false)
abstract class ManhwaDb : RoomDatabase() {
    abstract val manhwaDao: ManhwaDao
    abstract val characterDao : CharacterDao

    companion object {
        @Volatile
        private var INSTANCE: ManhwaDb? = null

        fun getInstance(context: Context): ManhwaDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ManhwaDb::class.java,
                        "manhwa_db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}