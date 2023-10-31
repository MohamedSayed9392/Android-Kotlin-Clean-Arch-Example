package com.example.burgers.data.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.burgers.domain.model.AssetsBurger
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetsBurgersDao {

    @Query("SELECT * FROM ${AssetsBurger.TABLE_NAME} WHERE link LIKE :startWithQuery")
    fun getAllBurgers(startWithQuery : String = "https://%"): Flow<List<AssetsBurger>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBurger(burger: AssetsBurger)
}