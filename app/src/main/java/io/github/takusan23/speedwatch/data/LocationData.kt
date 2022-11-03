package io.github.takusan23.speedwatch.data

import io.github.takusan23.speedwatch.tool.TimeTool

/**
 * 緯度経度を持っておくデータクラス
 */
data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val time: Long = System.currentTimeMillis(),
    val timeFormat: String = TimeTool.format(time),
)