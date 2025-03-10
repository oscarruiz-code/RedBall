package com.example.spartacusgame.utils

import android.content.Context
import android.media.MediaPlayer

class AudioManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null

    fun playLoopingAudio(resId: Int) {
        stopAudio()
        mediaPlayer = MediaPlayer.create(context, resId).apply {
            isLooping = true
            start()
        }
    }

    fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}