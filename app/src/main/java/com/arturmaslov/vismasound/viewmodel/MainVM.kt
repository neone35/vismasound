package com.arturmaslov.vismasound.viewmodel

import androidx.lifecycle.viewModelScope
import com.arturmaslov.vismasound.data.models.Track
import com.arturmaslov.vismasound.data.source.remote.LoadStatus
import com.arturmaslov.vismasound.data.usecase.GetRemoteGenreList
import com.arturmaslov.vismasound.data.usecase.GetRemoteTrackLists
import com.arturmaslov.vismasound.ui.compose.TrackSaveState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class MainVM(
    private val getRemoteTrackLists: GetRemoteTrackLists,
    private val getRemoteGenreList: GetRemoteGenreList
) : BaseVM() {

    private val genresTrackLists =
        MutableStateFlow<MutableMap<String, List<Track>?>?>(mutableMapOf())
    private val oneGenreTrackList =
        MutableStateFlow<List<Track>?>(emptyList())

    init {
        // runs every time VM is created (not view created)
        Timber.i("MainVM created!")
        fetchInitialGenresTrackLists()
    }

    private fun fetchInitialGenresTrackLists() {
        Timber.i("Running HomeVM updateLocalProductList")
        viewModelScope.launch {
            setLoadStatus(LoadStatus.LOADING)
            try {
                val genreList = getRemoteGenreList.execute()
                genreList
                    ?.filterNotNull()
                    ?.forEach { genre ->
                        setLoadStatus(LoadStatus.LOADING)
                        val oneGenreTrackList = getRemoteTrackLists.execute5(genre)
                        oneGenreTrackList?.let {
                            val tempGenresTrackLists = genresTrackLists.value
                            tempGenresTrackLists?.put(
                                genre,
                                it
                            )
                            genresTrackLists.value = tempGenresTrackLists
                            setLoadStatus(LoadStatus.DONE)
                        }
                    }
                setLoadStatus(LoadStatus.DONE)
            } catch (e: Exception) {
                setLoadStatus(LoadStatus.ERROR)
                Timber.e(e.localizedMessage!!)
            }
        }
    }

    private fun fetchOneGenreTracks(genre: String) {
        Timber.i("Running HomeVM fetchOneGenreTracks")
        viewModelScope.launch {
            setLoadStatus(LoadStatus.LOADING)
            try {
                val genreTrackList = getRemoteTrackLists.executeMax(genre)
                genreTrackList?.let {
                    oneGenreTrackList.value = genreTrackList
                }
                setLoadStatus(LoadStatus.DONE)
            } catch (e: Exception) {
                setLoadStatus(LoadStatus.ERROR)
                Timber.e(e.localizedMessage!!)
            }
        }
    }

    fun onSeeAllClicked(genre: String) {
        fetchOneGenreTracks(genre)
    }

    fun onTrackSaveClick(track: Track, trackSaveState: TrackSaveState) {

    }

    fun onTrackDeleteClick(track: Any) {

    }


    fun genresTrackLists() = genresTrackLists as StateFlow<Map<String, List<Track>?>?>
    fun oneGenreTrackList() = oneGenreTrackList as StateFlow<List<Track>?>

}