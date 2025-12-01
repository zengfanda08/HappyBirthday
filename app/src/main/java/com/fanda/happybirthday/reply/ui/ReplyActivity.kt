package com.fanda.happybirthday.reply.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import com.fanda.happybirthday.ui.theme.HappyBirthdayTheme

class ReplyActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class) override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            HappyBirthdayTheme {
                Scaffold {  innerPadding ->
                    val windowWSize = calculateWindowSizeClass(activity = this)
                    ReplyApp(windowWSize.widthSizeClass,modifier = Modifier
                        .padding(innerPadding))
                }

            }
        }
    }
}