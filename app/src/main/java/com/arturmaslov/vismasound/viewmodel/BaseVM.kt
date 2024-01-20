package com.arturmaslov.vismasound.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturmaslov.vismasound.App
import com.arturmaslov.vismasound.data.source.MainRepository
import com.arturmaslov.vismasound.data.source.remote.LoadStatus
import com.arturmaslov.vismasound.helpers.utils.NetworkChecker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

open class BaseVM(
    mainRepo: MainRepository? = null // provided by Koin
) : ViewModel() {

    val remoteResponse = mainRepo?.remoteResponse as SharedFlow<String?>?

    private val internetIsAvailable = MutableStateFlow(true)
    private val loadStatus = MutableStateFlow(LoadStatus.LOADING)

    init {
        // runs every time VM is created (not view created)
        viewModelScope.launch {
            loadStatus.emit(LoadStatus.LOADING)
            internetIsAvailable.value = NetworkChecker(App.getAppContext()).isNetworkConnected()
            loadStatus.emit(LoadStatus.DONE)
        }
    }

    fun setLoadStatus(status: LoadStatus) {
        viewModelScope.launch {
            Timber.i("Running BaseVM setBaseStatus with $status")
            loadStatus.emit(status)
        }
    }

    fun internetIsAvailable() = internetIsAvailable
    fun loadStatus() = loadStatus

}