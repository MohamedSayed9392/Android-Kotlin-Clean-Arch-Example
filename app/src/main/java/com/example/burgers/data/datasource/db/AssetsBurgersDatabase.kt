package com.example.burgers.data.datasource.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.burgers.data.datasource.dao.AssetsBurgersDao
import com.example.burgers.domain.model.AssetsBurger

@Database(entities = [AssetsBurger::class], version = 1, exportSchema = false)
abstract class AssetsBurgersDatabase : RoomDatabase() {

    abstract val burgersDao: AssetsBurgersDao

    companion object {
        val DATABASE_NAME = "assets_burger.db"
    }
}