package com.fanda.happybirthday.inventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao interface ItemDao {

    // 参数 onConflict 用于告知 Room 在发生冲突时应该执行的操作。OnConflictStrategy.IGNORE 策略会忽略新商品
    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun insert(item: Item)

    @Update suspend fun update(item: Item)

    @Delete suspend fun delete(item: Item)

    // 不用挂起函数，返回 flow 用于观察数据库中的商品
    @Query("SELECT * from items WHERE id = :id") fun getItem(id: Int): Flow<Item>

    @Query("SELECT * from items ORDER BY name ASC") fun getAllItems(): Flow<List<Item>>

}
