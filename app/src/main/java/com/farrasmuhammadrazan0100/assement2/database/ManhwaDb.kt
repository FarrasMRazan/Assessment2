package com.farrasmuhammadrazan0100.assement2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.farrasmuhammadrazan0100.assement2.model.CharacterEntity
import com.farrasmuhammadrazan0100.assement2.model.ManhwaEntity

@Database(
    entities = [ManhwaEntity::class, CharacterEntity::class],
    version = 2,
    exportSchema = false
)
abstract class ManhwaDb : RoomDatabase() {
    abstract val manhwaDao: ManhwaDao
    abstract val characterDao: CharacterDao

    companion object {
        @Volatile
        private var INSTANCE: ManhwaDb? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE manhwa_table ADD COLUMN userId TEXT NOT NULL DEFAULT ''"
                )
            }
        }

        fun getInstance(context: Context): ManhwaDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ManhwaDb::class.java,
                        "manhwa_db"
                    )
                        .addMigrations(MIGRATION_1_2)
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}