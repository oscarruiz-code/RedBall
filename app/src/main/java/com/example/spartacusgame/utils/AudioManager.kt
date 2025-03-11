package com.example.spartacusgame.utils

import android.content.Context
import android.media.MediaPlayer

/**
 * Clase reutilizable para manejar el audio en el programa
 *
 * @author oscarruiz-code
 *
 */
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