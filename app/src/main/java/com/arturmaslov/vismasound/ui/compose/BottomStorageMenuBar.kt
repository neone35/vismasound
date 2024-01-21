package com.arturmaslov.vismasound.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomStorageMenuBar() {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Temporary Storage
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable { /* TODO */ }
                .padding(8.dp)
        ) {
            Text(
                text = "Temporary Storage",
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Permanent Storage
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable { /* TODO */ }
                .padding(8.dp)
        ) {
            Text(
                text = "Permanent Storage",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}