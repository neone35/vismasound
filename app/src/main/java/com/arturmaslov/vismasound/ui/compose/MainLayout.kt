package com.arturmaslov.vismasound.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arturmaslov.vismasound.R
import com.arturmaslov.vismasound.data.models.Track
import com.arturmaslov.vismasound.helpers.extensions.formatDuration
import com.arturmaslov.vismasound.ui.MainActivity
import com.arturmaslov.vismasound.ui.theme.VismaSoundTheme
import com.arturmaslov.vismasound.ui.theme.seed
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.placeholder.placeholder.PlaceholderPlugin
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin

@Composable
@Preview(showBackground = true)
fun PreviewMusicGenreScreen() {
    VismaSoundTheme {
        MainAllGenreScreen(
            navController = null,
            genresTrackLists = mapOf(
                "Rock" to listOf(
                    Track(
                        title = "dasdasdasd asdasdasdas",
                        duration = "151665"
                    ),
                    Track(),
                    Track()
                ),
                "Ambient" to listOf(
                    Track(),
                    Track(),
                    Track()
                )
            ),
            onSeeAllClick = {}
        )
    }
}

@Composable
fun MainAllGenreScreen(
    navController: NavController?,
    genresTrackLists: Map<String, List<Track>?>?,
    onSeeAllClick: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
        ) {
            genresTrackLists?.forEach { (genreTitle, songs) ->
                item {
                    MusicGenreSection(
                        title = genreTitle,
                        tracks = songs.orEmpty(),
                        navController = navController,
                        onSeeAllClick = onSeeAllClick
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun MusicGenreSection(
    title: String,
    tracks: List<Track>,
    navController: NavController?,
    onSeeAllClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    onSeeAllClick(title)
                    navController
                        ?.navigate(MainActivity.GENRE_SCREEN + "/" + title)
                }
            ) {
                Text("See All")
            }
        }
        LazyRow {
            itemsIndexed(tracks) { index, song ->
                TrackCard(track = song)
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackCard(track: Track) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .defaultMinSize(
                minHeight = 80.dp
            ),
        onClick = { /* TODO */ }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            GlideImage(
                imageModel = { track.imgUrl },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                component = rememberImageComponent {
                    +PlaceholderPlugin.Failure(painterResource(id = R.drawable.ic_logo))
                    +ShimmerPlugin(
                        baseColor = MaterialTheme.colorScheme.onPrimary,
                        highlightColor = seed
                    )
                },
                previewPlaceholder = R.drawable.ic_logo,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = (track.title?.take(20) + "..."),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = track.duration?.formatDuration()
                            ?: 0.toString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = track.duration?.formatDuration()
                            ?: 0.toString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}