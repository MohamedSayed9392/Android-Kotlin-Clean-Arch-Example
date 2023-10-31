package com.example.burgers.core.di

import android.app.Application
import androidx.room.Room
import com.example.burgers.data.datasource.api.BurgersApi
import com.example.burgers.data.datasource.db.AssetsBurgersDatabase
import com.example.burgers.data.repository.AssetsBurgerRepositoryImpl
import com.example.burgers.domain.repository.AssetsBurgerRepository
import com.example.burgers.domain.usecase.BurgerUseCases
import com.example.burgers.domain.usecase.GetAllBurgersDB
import com.example.burgers.domain.usecase.GetBurgersRemote
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAssetsBurgerDatabase(app: Application): AssetsBurgersDatabase {
        return Room.databaseBuilder(
            app,
            AssetsBurgersDatabase::class.java,
            AssetsBurgersDatabase.DATABASE_NAME
        ).createFromAsset("dbs/burger_task.db")
            .build()
    }

    @Provides
    @Singleton
    fun provideAssetsBurgerRepository(db: AssetsBurgersDatabase,api:BurgersApi): AssetsBurgerRepository {
        return AssetsBurgerRepositoryImpl(db.burgersDao,api)
    }

    @Provides
    @Singleton
    fun provideAssetsBurgerUseCases(repository: AssetsBurgerRepository): BurgerUseCases {
        return BurgerUseCases(
            getAllBurgersDB = GetAllBurgersDB(repository),
            getBurgersRemote = GetBurgersRemote(repository),
        )
    }
}