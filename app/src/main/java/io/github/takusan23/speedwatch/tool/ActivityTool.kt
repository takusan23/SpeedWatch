package io.github.takusan23.speedwatch.tool

import android.app.Activity
import android.view.WindowManager

object ActivityTool {

    /**
     * スリープにしないようにする
     *
     * @param activity [Activity]
     * @param isAOD trueでスリープにしない
     */
    fun setSleepBlock(activity: Activity, isAOD: Boolean) {
        if (isAOD) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

}