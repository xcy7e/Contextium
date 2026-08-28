package com.xcy7e.contextium

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ContextMenuItem::class],
    version = 1,
    exportSchema = false
)
abstract class ContextiumDatabase : RoomDatabase() {

    abstract fun contextMenuItemDao(): ContextMenuItemDao

    companion object {
        @Volatile
        private var instance: ContextiumDatabase? = null

        fun getInstance(context: Context): ContextiumDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ContextiumDatabase::class.java,
                    "contextium.db"
                ).build().also { instance = it }
            }
        }
    }
}