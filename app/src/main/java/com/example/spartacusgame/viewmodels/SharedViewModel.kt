package com.example.spartacusgame.viewmodels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class SharedViewModel : ViewModel() {
    private var _playerName by mutableStateOf("")
    var playerName: String
        get() = _playerName
        set(value) {

            if (value.isNotBlank()) {
                _playerName = value
            }
        }
}