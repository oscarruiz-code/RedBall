package com.example.spartacusgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.spartacusgame.navigation.AppNavigation
import com.example.spartacusgame.ui.theme.SpartacusTheme

class MainActivity : ComponentActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpartacusTheme {
                AppNavigation(sharedViewModel = sharedViewModel)
            }
        }
    }
}
