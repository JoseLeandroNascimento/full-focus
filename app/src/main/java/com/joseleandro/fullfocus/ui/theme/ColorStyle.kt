package com.joseleandro.fullfocus.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.fromColorLong
import androidx.compose.ui.graphics.toColorLong
import kotlinx.serialization.Serializable

@Serializable
sealed class ColorStyle {
    @Serializable
    data class Solid(val colorLong: Long) : ColorStyle()

    @Serializable
    data class Gradient(val colorsLong: List<Long>) : ColorStyle()

    fun asBrush(): Brush {
        return when (this) {
            is Solid -> Brush.linearGradient(listOf(Color.fromColorLong(colorLong), Color.fromColorLong(colorLong)))
            is Gradient -> Brush.linearGradient(colorsLong.map { Color.fromColorLong(it) })
        }
    }

    fun getPrimaryColor(): Color {
        return when (this) {
            is Solid -> Color.fromColorLong(colorLong)
            is Gradient -> Color.fromColorLong(colorsLong.first())
        }
    }

    companion object {
        fun fromColor(color: Color) = Solid(color.toColorLong())
        fun fromColors(colors: List<Color>) = Gradient(colors.map { it.toColorLong() })
    }
}
