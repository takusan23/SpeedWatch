package io.github.takusan23.speedwatch.tool

import android.icu.text.SimpleDateFormat

object TimeTool {

    /** 日付の形式 */
    private val simpleDateFormat = SimpleDateFormat("HH:mm:ss.sss")

    /** ミリ秒を人間が読めるフォーマットにする */
    fun format(timeMs: Long) = simpleDateFormat.format(timeMs)

}