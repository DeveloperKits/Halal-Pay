package com.recogniseerror.CashNet.Model

import android.content.Context
import android.media.MediaPlayer
import com.recogniseerror.CashNet.R

class MediaSound {
    var mediaPlayer = MediaPlayer()

    fun buttonSound(con: Context){
        mediaPlayer = MediaPlayer.create(con, R.raw.click_sound)
        val maxVolume = 70.0f
        val currentVolume = 30.0f

        mediaPlayer.setVolume(currentVolume / maxVolume, currentVolume / maxVolume)
        mediaPlayer.start()

    }
}