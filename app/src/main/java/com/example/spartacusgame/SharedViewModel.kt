package com.example.spartacusgame

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class SharedViewModel : ViewModel() {
    var background by mutableStateOf("first")
        private set

    fun changeBackground(newBackground: String) {
        background = newBackground
    }
}
