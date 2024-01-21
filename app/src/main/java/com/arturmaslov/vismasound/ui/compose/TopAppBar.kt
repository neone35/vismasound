package com.arturmaslov.vismasound.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arturmaslov.vismasound.R
import com.arturmaslov.vismasound.helpers.utils.Constants
import com.arturmaslov.vismasound.ui.theme.VismaSoundTheme
import com.arturmaslov.vismasound.ui.theme.seed

@Preview(showBackground = true)
@Composable
fun SkubaTopAppBarPreview() {
    VismaSoundTheme {
        VismaTopAppBar(

        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VismaTopAppBar(

) {
    TopAppBar(
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = Constants.EMPTY_STRING,
                contentScale = ContentScale.None,
                modifier = Modifier.size(40.dp),
                colorFilter = ColorFilter.tint(seed),
            )
        },
        title = {
            Text(
                stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {

        }
    )
}