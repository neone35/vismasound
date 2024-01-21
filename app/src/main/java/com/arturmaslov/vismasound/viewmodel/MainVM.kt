package com.arturmaslov.vismasound.viewmodel

import androidx.lifecycle.viewModelScope
import com.arturmaslov.vismasound.data.models.Track
import com.arturmaslov.vismasound.data.source.remote.LoadStatus
import com.arturmaslov.vismasound.data.usecase.GetRemoteGenreList
import com.arturmaslov.vismasound.data.usecase.GetRemoteTrackLists
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class MainVM(
    private val getRemoteTrackLists: GetRemoteTrackLists,
    private val getRemoteGenreList: GetRemoteGenreList
) : BaseVM() {

//    private var initialProductList: List<Product?>? = emptyList()
//    private val startProductList = MutableStateFlow<List<Product?>?>(emptyList())
//    private val finalProductList = MutableStateFlow<List<Product?>?>(emptyList())
//    private val productSortOption = MutableStateFlow(ProductSortOption.BRAND)

    private val genresTrackLists =
        MutableStateFlow<MutableMap<String, List<Track>?>?>(mutableMapOf())

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
                val genreList = getRemoteGenreList.execute()
                genreList
                    ?.filterNotNull()
                    ?.forEach { genre ->
                        val oneGenreTrackList = getRemoteTrackLists.execute5(genre)
                        oneGenreTrackList?.let {
                            val tempGenresTrackLists = genresTrackLists.value
                            tempGenresTrackLists?.put(
                                genre,
                                it
                            )
                            genresTrackLists.value = tempGenresTrackLists
                        }
                    }

                setLoadStatus(LoadStatus.DONE)
            } catch (e: Exception) {
                setLoadStatus(LoadStatus.ERROR)
                Timber.e(e.localizedMessage!!)
            }
        }
    }

    fun genresTrackLists() = genresTrackLists as StateFlow<Map<String, List<Track>?>?>


}