package com.arturmaslov.vismasound.di

import com.arturmaslov.vismasound.data.usecase.GetRemoteGenresTrackLists
import org.koin.dsl.module

val useCaseModule = module {
    factory {
        GetRemoteGenresTrackLists(get())
    }
}

