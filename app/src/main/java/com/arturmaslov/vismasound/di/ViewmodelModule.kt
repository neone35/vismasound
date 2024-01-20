package com.arturmaslov.vismasound.di

import com.arturmaslov.vismasound.viewmodel.BaseVM
import com.arturmaslov.vismasound.viewmodel.MainVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { BaseVM(get()) }
    viewModel {
        MainVM(

        )
    }
}