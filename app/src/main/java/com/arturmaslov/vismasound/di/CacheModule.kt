package com.arturmaslov.vismasound.di

import android.content.Context
import com.arturmaslov.vismasound.helpers.cache.TokenTimeCache
import com.arturmaslov.vismasound.helpers.cache.TokenTimeCacheImpl
import com.arturmaslov.vismasound.helpers.cache.TokenTimeCacheManager
import com.arturmaslov.vismasound.helpers.utils.Constants
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val cacheModule = module {
    single(named(Constants.PREFS_DEFAULT)) {
        androidContext().getSharedPreferences(
            Constants.PREFS_DEFAULT,
            Context.MODE_PRIVATE
        )
    }

    single<TokenTimeCache> {
        TokenTimeCacheImpl(
            dispatcher = Dispatchers.IO,
            sharedPreferences = get(named(Constants.PREFS_DEFAULT))
        )
    }

    single { provideTokenCacheManager(get()) }
}

private fun provideTokenCacheManager(
    tokenTimeCache: TokenTimeCache
): TokenTimeCacheManager {
    return TokenTimeCacheManager(tokenTimeCache)
}