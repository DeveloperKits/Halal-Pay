package com.samulit.halal_pay.Model

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.preference.PreferenceManager
import com.samulit.halal_pay.R

class MediaSound {
    var mediaPlayer = MediaPlayer()

    fun buttonSound(con: Context){
        val preferences = PreferenceManager.getDefaultSharedPreferences(con)
        val isChecked = preferences.getBoolean("isSoundChecked", false)

        if (isChecked) {
            mediaPlayer = MediaPlayer.create(con, R.raw.click_sound)
            mediaPlayer.start()
        }
    }
}