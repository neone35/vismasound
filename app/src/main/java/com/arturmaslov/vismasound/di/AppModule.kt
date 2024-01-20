package com.arturmaslov.vismasound.di

import android.content.Context
import com.arturmaslov.vismasound.BuildConfig
import com.arturmaslov.vismasound.data.source.remote.Api
import com.arturmaslov.vismasound.data.source.remote.SoundCloudApiService
import com.arturmaslov.vismasound.helpers.utils.Constants
import com.arturmaslov.vismasound.helpers.utils.NetworkChecker
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

val appModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
    single { provideApiService(get()) }
    single { provideNetworkChecker(androidContext()) }
    single { Api(get()) }
}

private fun provideNetworkChecker(context: Context) = NetworkChecker(context)

private fun provideApiService(retrofit: Retrofit): SoundCloudApiService =
    retrofit.create(SoundCloudApiService::class.java)

private fun provideRetrofit(
    okHttpClient: OkHttpClient
): Retrofit {
    val baseUrl = Constants.SOUNDCLOUD_BASE_URL
    Timber.d("baseUrl is $baseUrl")

    val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
    val retrofit: Retrofit = if (BuildConfig.DEBUG) {
        retrofitBuilder.client(okHttpClient)
        retrofitBuilder.build()
    } else {
        retrofitBuilder.build()
    }
    return retrofit
}

private fun provideOkHttpClient(): OkHttpClient {
    val clientBuilder = OkHttpClient.Builder()
        .connectTimeout(Constants.REMOTE_TIMEOUT_SEC, TimeUnit.SECONDS)
        .readTimeout(Constants.REMOTE_TIMEOUT_SEC, TimeUnit.SECONDS)

    if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        clientBuilder.addInterceptor(loggingInterceptor)
    }

    return clientBuilder.build()
}