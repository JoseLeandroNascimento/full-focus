package com.joseleandro.fullfocus.core.util

import android.content.Context
import android.media.MediaPlayer
import android.util.Log

class BackgroundSoundPlayer(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var currentResId: Int? = null

    fun play(resId: Int, volume: Float, looping: Boolean = true) {
        try {
            if (currentResId == resId && mediaPlayer != null) {
                updateVolume(volume)
                return
            }

            stop()
            currentResId = resId
            mediaPlayer = MediaPlayer.create(context, resId)?.apply {
                setVolume(volume, volume)
                isLooping = looping
                start()
            }
        } catch (e: Exception) {
            Log.e("BackgroundSoundPlayer", "Error playing sound", e)
            stop()
        }
    }

    fun stop() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (e: Exception) {
            Log.e("BackgroundSoundPlayer", "Error stopping sound", e)
        } finally {
            mediaPlayer = null
            currentResId = null
        }
    }

    fun updateVolume(volume: Float) {
        try {
            mediaPlayer?.setVolume(volume, volume)
        } catch (e: Exception) {
            Log.e("BackgroundSoundPlayer", "Error updating volume", e)
        }
    }

    fun release() {
        stop()
    }
}
