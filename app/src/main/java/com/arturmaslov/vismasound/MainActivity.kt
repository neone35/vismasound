package com.arturmaslov.vismasound

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arturmaslov.vismasound.ui.compose.MainLayout
import com.arturmaslov.vismasound.ui.theme.VismaSoundTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            VismaSoundTheme {
                MainLayout()
            }
        }
    }
}