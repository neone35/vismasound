package com.arturmaslov.vismasound.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun BottomStorageMenuBarPreview() {
    BottomStorageMenuBar(
        tempTrackSumDuration = "25min 41s",
        permTrackSumDuration = "55min 14s",
    )
}

@Composable
fun BottomStorageMenuBar(
    tempTrackSumDuration: String,
    permTrackSumDuration: String
) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Temporary Storage
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .clickable { /* TODO */ }
                .fillMaxHeight()
        ) {
            Text(
                text = "Temporary Storage",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = tempTrackSumDuration,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        // Permanent Storage
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .clickable { /* TODO */ }
                .fillMaxHeight()
        ) {
            Text(
                text = "Permanent Storage",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = permTrackSumDuration,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}