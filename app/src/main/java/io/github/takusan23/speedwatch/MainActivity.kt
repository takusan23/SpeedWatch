/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package io.github.takusan23.speedwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import io.github.takusan23.speedwatch.tool.PermissionTool
import io.github.takusan23.speedwatch.ui.screen.LicenseScreen
import io.github.takusan23.speedwatch.ui.screen.PermissionScreen
import io.github.takusan23.speedwatch.ui.screen.SettingScreen
import io.github.takusan23.speedwatch.ui.screen.SpeedScreen
import io.github.takusan23.speedwatch.ui.theme.SpeedWatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp()
        }
    }
}

object Navigates {
    /** 権限画面 */
    const val PermissionScreen = "permission"

    /** ホーム */
    const val SpeedScreen = "speed"

    /** 設定 */
    const val SettingScreen = "setting"

    /** ライセンス */
    const val LicenseScreen = "license"
}

@Composable
fun WearApp() {
    SpeedWatchTheme {
        val context = LocalContext.current
        val navController = rememberSwipeDismissableNavController()
        val start = if (PermissionTool.isGrantedLocationPermission(context)) Navigates.SpeedScreen else Navigates.PermissionScreen
        SwipeDismissableNavHost(navController = navController, startDestination = start) {
            composable(Navigates.PermissionScreen) {
                PermissionScreen { navController.navigate(Navigates.SpeedScreen) }
            }
            composable(Navigates.SpeedScreen) {
                SpeedScreen { navController.navigate(it) }
            }
            composable(Navigates.SettingScreen) {
                SettingScreen { navController.navigate(it) }
            }
            composable(Navigates.LicenseScreen) {
                LicenseScreen()
            }
        }
    }
}