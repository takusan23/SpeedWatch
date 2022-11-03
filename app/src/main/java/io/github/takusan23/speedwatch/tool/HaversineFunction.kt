package io.github.takusan23.speedwatch.tool

import kotlin.math.*

/**
 * 半正矢関数の公式 を利用して、二点間の座標の距離を出す
 * Haversine とかで調べて
 */
object HaversineFunction {

    /** Radious of the earth */
    private const val R = 6371

    /** ２つの緯度経度の距離をキロメートルで返す */
    fun calc(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double,
    ): Double {
        val latDistance = (lat2 - lat1).toRad()
        val lonDistance = (lon2 - lon1).toRad()
        val a = sin(latDistance / 2) * sin(latDistance / 2) + cos(lat1.toRad()) * cos(lat2.toRad()) * sin(lonDistance / 2) * sin(lonDistance / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distance = R * c
        return distance
    }

    private fun Double.toRad(): Double {
        return this * PI / 180
    }
}