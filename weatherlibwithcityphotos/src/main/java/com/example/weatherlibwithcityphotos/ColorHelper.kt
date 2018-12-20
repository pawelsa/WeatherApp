package com.example.weatherlibwithcityphotos

import android.graphics.Color
import android.util.Log

class ColorHelper {

    companion object {

        private val MAX_TEMP = 40
        private val MIN_TEMP = -20
        private val TEMP_DIFF = MAX_TEMP - MIN_TEMP
        private val HOT_COLOR = Color.parseColor("#FF8000")
        private val COLD_COLOR = Color.parseColor("#165B88")
        val START_COLOR = colorToBasic(HOT_COLOR)
        val END_COLOR = colorToBasic(COLD_COLOR)


        private fun colorToBasic(color: Int): IntArray {

            val alpha = Color.alpha(color)
            val red = Color.red(color)
            val green = Color.green(color)
            val blue = Color.blue(color)

            return intArrayOf(alpha, red, green, blue)
        }

        fun pickHex(progress: Float): Int {
            return pickHex(START_COLOR, END_COLOR, progress, 1 - progress)
        }

        fun pickHex(color1: IntArray, color2: IntArray, weight: Float, restWeight: Float = 1 - weight): Int {
            Log.i("Color", "Progress : $weight rest : $restWeight")
            return Color.argb(Math.round(color2[0] * weight + color1[0] * restWeight),
                    Math.round(color2[1] * weight + color1[1] * restWeight),
                    Math.round(color2[2] * weight + color1[2] * restWeight),
                    Math.round(color2[3] * weight + color1[3] * restWeight))
        }

        fun colorForTemperature(temp: Float): Int {
            return when {
                temp > MAX_TEMP -> HOT_COLOR
                temp < MIN_TEMP -> COLD_COLOR
                else -> {
                    val tempPercent = 1 - Math.abs(temp / TEMP_DIFF)
                    pickHex(START_COLOR, END_COLOR, tempPercent)
                }
            }
        }

        fun getThumbColor(progress: Int, tempArray: IntArray): Int {
            return shadeColor(getTempColor(progress, tempArray))
        }

        fun getTempColor(progress: Int, tempArray: IntArray): Int {
            return when {
                progress < 25 -> {
                    pickHex(colorToBasic(tempArray[0]), colorToBasic(tempArray[1]), progress / 5f)
                }
                progress < 50 -> {
                    pickHex(colorToBasic(tempArray[1]), colorToBasic(tempArray[2]), progress / 5f)
                }
                progress < 75 -> {
                    pickHex(colorToBasic(tempArray[2]), colorToBasic(tempArray[3]), progress / 5f)
                }
                else -> {
                    pickHex(colorToBasic(tempArray[3]), colorToBasic(tempArray[4]), progress / 5f)
                }
            }
        }

        private fun shadeColor(color: Int): Int {
            val base = colorToBasic(color)
            for (i in 1 until base.size) {
                base[i] += base[i] * 1 / 3
            }
            return Color.argb(base[0], base[1], base[2], base[3])
        }
    }
}