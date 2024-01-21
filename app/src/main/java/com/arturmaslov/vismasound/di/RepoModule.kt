package com.arturmaslov.vismasound.di

import android.app.Application
import androidx.room.Room
import com.arturmaslov.vismasound.data.source.MainRepository
import com.arturmaslov.vismasound.data.source.local.LocalDataSource
import com.arturmaslov.vismasound.data.source.local.LocalDatabase
import com.arturmaslov.vismasound.data.source.remote.Api
import com.arturmaslov.vismasound.data.source.remote.RemoteDataSource
import com.arturmaslov.vismasound.helpers.utils.TokenTimeCacheManager
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val repoModule = module {
    single { provideLocalDatabase(androidApplication()) }
    single { provideLocalDataSource(get()) }
    single { provideRemoteDataSource(get(), get()) }
    single { provideMainRepository(get(), get()) }
}

fun provideLocalDatabase(app: Application): LocalDatabase {
    val dbName = LocalDatabase.DATABASE_NAME
    return Room.databaseBuilder(
        app,
        LocalDatabase::class.java, dbName
    )
        .fallbackToDestructiveMigration()
        .build()
}

private fun provideLocalDataSource(
    localDB: LocalDatabase
): LocalDataSource {
    return LocalDataSource(localDB, Dispatchers.IO)
}

private fun provideRemoteDataSource(
    api: Api,
    tokenTimeCacheManager: TokenTimeCacheManager
): RemoteDataSource {
    return RemoteDataSource(api, Dispatchers.IO, tokenTimeCacheManager)
}

private fun provideMainRepository(
    localDataSource: LocalDataSource,
    remoteDataSource: RemoteDataSource
): MainRepository {
    return MainRepository(localDataSource, remoteDataSource)
}
