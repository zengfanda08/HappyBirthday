package com.fanda.happybirthday.race

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.Modifier
import com.fanda.happybirthday.ui.theme.HappyBirthdayTheme

class RaceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            HappyBirthdayTheme {
                Scaffold {  innerPadding ->
                    RaceTrackerApp(modifier = Modifier
                        .padding(innerPadding))
                }

            }
        }
    }
}