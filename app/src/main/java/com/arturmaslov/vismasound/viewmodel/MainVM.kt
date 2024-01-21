package com.arturmaslov.vismasound.viewmodel

import androidx.lifecycle.viewModelScope
import com.arturmaslov.vismasound.data.models.Track
import com.arturmaslov.vismasound.data.source.remote.LoadStatus
import com.arturmaslov.vismasound.data.usecase.GetRemoteGenreList
import com.arturmaslov.vismasound.data.usecase.GetRemoteTrackLists
import com.arturmaslov.vismasound.data.usecase.ManageLocalTracks
import com.arturmaslov.vismasound.helpers.extensions.formatDuration
import com.arturmaslov.vismasound.ui.compose.TrackSaveState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class MainVM(
    private val getRemoteTrackLists: GetRemoteTrackLists,
    private val getRemoteGenreList: GetRemoteGenreList,
    private val manageLocalTracks: ManageLocalTracks
) : BaseVM() {

    private val tempTrackList: MutableList<Track> = mutableListOf()

    private val genresTrackLists =
        MutableStateFlow<MutableMap<String, List<Track>?>?>(mutableMapOf())
    private val oneGenreTrackList =
        MutableStateFlow<List<Track>?>(emptyList())

    private val tempTrackListLength = MutableStateFlow("0min 0s")
    private val permTrackListLength = MutableStateFlow("0min 0s")

    init {
        // runs every time VM is created (not view created)
        Timber.i("MainVM created!")
        viewModelScope.launch {
            fetchInitialGenresTrackLists()
            updateTempTrackListLength()
            updatePermTrackListLength()
        }
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
                    val permTrackList = manageLocalTracks.executeGetAll()
                    val oneGenreListWithUpdatedSaveStates = genreTrackList.map { track ->
                        track.saveState = when {
                            tempTrackList.any { it.id == track.id } -> TrackSaveState.TEMPORARY
                            permTrackList?.any { it.id == track.id } == true -> TrackSaveState.PERMANENT
                            else -> TrackSaveState.NOT_SAVED
                        }
                        track
                    }
                    oneGenreTrackList.value = oneGenreListWithUpdatedSaveStates
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

    fun onTrackSaveStateClick(track: Track, trackSaveState: TrackSaveState) {
        when (trackSaveState) {
            TrackSaveState.NOT_SAVED -> {
                // save to temporary
                val alreadyAdded = tempTrackList.contains(track)
                if (!alreadyAdded) {
                    val updatedTrack = track.copy(saveState = TrackSaveState.TEMPORARY)
                    tempTrackList.add(updatedTrack)
                }
                updateTempTrackListLength()
            }

            TrackSaveState.TEMPORARY -> {
                // save to permanent
                viewModelScope.launch {
                    val localTracks = manageLocalTracks.executeGetAll()
                    val alreadyAdded = localTracks?.contains(track) ?: false
                    if (!alreadyAdded) {
                        manageLocalTracks.executeInsertOne(track)
                    }
                    updatePermTrackListLength()
                }
            }

            TrackSaveState.PERMANENT -> {
                // delete from everywhere
                // changes to NOT_SAVED
                viewModelScope.launch {
                    manageLocalTracks.deleteOne(track)
                    updatePermTrackListLength()
                    tempTrackList.removeIf { it.id == track.id }
                    updateTempTrackListLength()
                }
            }
        }
    }

    private fun updateTempTrackListLength() {
        val sumOfLengths = tempTrackList
            .mapNotNull { it.duration?.toLong() }
            .sum()

        tempTrackListLength.value = sumOfLengths.toString().formatDuration()
    }

    private fun updatePermTrackListLength() {
        viewModelScope.launch {
            val allPermTracks = manageLocalTracks.executeGetAll()
            val sumOfLengths = allPermTracks
                ?.mapNotNull { it.duration?.toLong() }
                ?.sum()

            permTrackListLength.value = sumOfLengths.toString().formatDuration()
        }
    }


    fun genresTrackLists() = genresTrackLists as StateFlow<Map<String, List<Track>?>?>
    fun oneGenreTrackList() = oneGenreTrackList as StateFlow<List<Track>?>
    fun tempTrackListLength() = tempTrackListLength as StateFlow<String>
    fun permTrackListLength() = permTrackListLength as StateFlow<String>

}