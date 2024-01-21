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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.arturmaslov.vismasound.R
import com.arturmaslov.vismasound.ui.MainActivity
import com.arturmaslov.vismasound.viewmodel.MainVM

@Composable
@Preview
fun BottomStorageMenuBarPreview() {
    BottomStorageMenuBar(
        tempTrackSumDuration = "25min 41s",
        permTrackSumDuration = "55min 14s",
        tempStorageSelected = {},
        permStorageSelected = {},
        navController = null
    )
}

@Composable
fun BottomStorageMenuBar(
    tempTrackSumDuration: String,
    permTrackSumDuration: String,
    tempStorageSelected: () -> Unit,
    permStorageSelected: () -> Unit,
    navController: NavController?,
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
                .clickable {
                    tempStorageSelected()
                    navController
                        ?.navigate(MainActivity.GENRE_SCREEN + "/" + MainVM.TEMP_STORAGE)
                }
                .fillMaxHeight()
        ) {
            Text(
                text = stringResource(R.string.temporary_storage),
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
                .clickable {
                    permStorageSelected()
                    navController
                        ?.navigate(MainActivity.GENRE_SCREEN + "/" + MainVM.PERM_STORAGE)
                }
                .fillMaxHeight()
        ) {
            Text(
                text = stringResource(R.string.permanent_storage),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = permTrackSumDuration,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}