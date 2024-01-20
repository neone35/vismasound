package com.arturmaslov.vismasound.viewmodel

import androidx.lifecycle.viewModelScope
import com.arturmaslov.vismasound.data.models.Track
import com.arturmaslov.vismasound.data.source.remote.LoadStatus
import com.arturmaslov.vismasound.data.usecase.GetRemoteGenresTrackLists
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class MainVM(
    private val getRemoteGenresTrackLists: GetRemoteGenresTrackLists,
) : BaseVM() {

//    private var initialProductList: List<Product?>? = emptyList()
//    private val startProductList = MutableStateFlow<List<Product?>?>(emptyList())
//    private val finalProductList = MutableStateFlow<List<Product?>?>(emptyList())
//    private val productSortOption = MutableStateFlow(ProductSortOption.BRAND)

    private val genresTrackLists = MutableStateFlow<List<Track?>?>(emptyList())

    init {
        // runs every time VM is created (not view created)
        Timber.i("MainVM created!")
        fetchRemoteGenresTrackLists()
    }

    private fun fetchRemoteGenresTrackLists() {
        Timber.i("Running HomeVM updateLocalProductList")
        viewModelScope.launch {
            setLoadStatus(LoadStatus.LOADING)
            try {
                genresTrackLists.value = getRemoteGenresTrackLists.execute("rock")
                setLoadStatus(LoadStatus.DONE)
            } catch (e: Exception) {
                setLoadStatus(LoadStatus.ERROR)
                Timber.e(e.localizedMessage!!)
            }
        }
    }

    fun genresTrackLists() = genresTrackLists as StateFlow<List<Track?>?>


}