package com.arturmaslov.vismasound.di

import android.app.Application
import androidx.room.Room
import com.arturmaslov.vismasound.data.source.MainRepository
import com.arturmaslov.vismasound.data.source.local.LocalDataSource
import com.arturmaslov.vismasound.data.source.local.LocalDatabase
import com.arturmaslov.vismasound.data.source.remote.Api
import com.arturmaslov.vismasound.data.source.remote.RemoteDataSource
import com.arturmaslov.vismasound.helpers.utils.Constants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val repoModule = module {
    single { provideLocalDatabase(androidApplication()) }
    single { provideDispatcherIO() }
    single { provideMainRepository(get(), get(), get()) }
    single { provideLocalDataSource(get(), get()) }
    single { provideRemoteDataSource(get(), get()) }
}

fun provideLocalDatabase(app: Application): LocalDatabase {
    val dbName = Constants.DATABASE_NAME
    return Room.databaseBuilder(
        app,
        LocalDatabase::class.java, dbName
    )
        .fallbackToDestructiveMigration()
        .build()
}

private fun provideDispatcherIO() = Dispatchers.IO

private fun provideMainRepository(
    dispatcher: CoroutineDispatcher,
    api: Api,
    localDB: LocalDatabase
): MainRepository {
    val localDataSource = provideLocalDataSource(localDB, dispatcher)
    val remoteDataSource = provideRemoteDataSource(api, dispatcher)
    return MainRepository(localDataSource, remoteDataSource)
}

private fun provideLocalDataSource(
    localDB: LocalDatabase,
    dispatcher: CoroutineDispatcher
): LocalDataSource {
    return LocalDataSource(localDB, dispatcher)
}

private fun provideRemoteDataSource(
    api: Api,
    dispatcher: CoroutineDispatcher
): RemoteDataSource {
    return RemoteDataSource(api, dispatcher)
}