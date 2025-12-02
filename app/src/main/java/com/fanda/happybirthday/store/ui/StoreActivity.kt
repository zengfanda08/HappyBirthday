package com.fanda.happybirthday.store.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fanda.happybirthday.cupcake.ui.screen.CupcakeApp
import com.fanda.happybirthday.ui.theme.HappyBirthdayTheme

class StoreActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HappyBirthdayTheme {
                DessertReleaseApp()
            }
        }
    }
}