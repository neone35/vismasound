package com.arturmaslov.vismasound.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arturmaslov.vismasound.R
import com.arturmaslov.vismasound.data.models.Track
import com.arturmaslov.vismasound.helpers.extensions.formatDuration
import com.arturmaslov.vismasound.helpers.utils.Constants

@Composable
@Preview
fun SongListScreenPreview() {
    val sampleTracks = listOf(
        Track(
            title = "dasdasdasd asdasdasdas",
            duration = "151665"
        ),
        Track(
            title = "dasdasdasd asdasdasdas",
            duration = "151665"
        )
    )

    OneGenreLayout(
        songList = sampleTracks,
        genre = "Pop",
        onSaveOptionSelected = { _, _ -> }
    )
}

@Composable
fun OneGenreLayout(
    songList: List<Track>,
    genre: String,
    onSaveOptionSelected: (Track, TrackSaveState) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title with genre
        Text(
            text = genre,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Song list
        LazyColumn {
            itemsIndexed(songList) { index, track ->
                TrackListItem(
                    track = track,
                    onSaveOptionSelected = onSaveOptionSelected,
                )
            }
        }
    }
}

@Composable
fun TrackListItem(
    track: Track,
    onSaveOptionSelected: (Track, TrackSaveState) -> Unit,
) {
    var trackSaveState by remember { mutableStateOf(track.saveState) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Song title
        Text(
            text = track.title ?: Constants.EMPTY_STRING,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )

        // Size and duration
        Column {
            Text(
                text = "${track.duration?.formatDuration()}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "${track.duration?.formatDuration()}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        when (trackSaveState) {
            TrackSaveState.NOT_SAVED -> {
                Icon(
                    painter = painterResource(id = R.drawable.ic_floppy),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            onSaveOptionSelected(track, TrackSaveState.NOT_SAVED)
                            trackSaveState = TrackSaveState.TEMPORARY
                        }
                )
            }

            TrackSaveState.TEMPORARY -> {
                Icon(
                    painter = painterResource(id = R.drawable.ic_auto_delete),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            onSaveOptionSelected(track, TrackSaveState.TEMPORARY)
                            trackSaveState = TrackSaveState.PERMANENT
                        }
                )
            }

            TrackSaveState.PERMANENT -> {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            onSaveOptionSelected(track, TrackSaveState.PERMANENT)
                            trackSaveState = TrackSaveState.NOT_SAVED
                        }
                )
            }
        }
    }
}

enum class TrackSaveState {
    NOT_SAVED,
    TEMPORARY,
    PERMANENT
}