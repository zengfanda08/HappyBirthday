package com.fanda.happybirthday.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1, exportSchema = false) abstract class InventoryDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile private var Instance: InventoryDatabase? = null

        fun getDatabase(context: Context): InventoryDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext, InventoryDatabase::class.java, "inventory_database"
                ).fallbackToDestructiveMigration().build() // 允许销毁并重建数据库
                    .also { Instance = it }
            }
        }
    }
}