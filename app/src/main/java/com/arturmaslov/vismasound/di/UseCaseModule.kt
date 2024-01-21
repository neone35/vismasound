package com.arturmaslov.vismasound.di

import com.arturmaslov.vismasound.data.usecase.GetRemoteGenreList
import com.arturmaslov.vismasound.data.usecase.GetRemoteTrackLists
import org.koin.dsl.module

val useCaseModule = module {
    factory {
        GetRemoteTrackLists(get())
    }
    factory {
        GetRemoteGenreList(get())
    }
}

