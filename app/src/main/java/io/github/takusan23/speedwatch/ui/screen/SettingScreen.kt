package io.github.takusan23.speedwatch.ui.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.wear.compose.material.*
import androidx.wear.remote.interactions.RemoteActivityHelper
import io.github.takusan23.speedwatch.Navigates
import io.github.takusan23.speedwatch.R
import io.github.takusan23.speedwatch.ui.component.WatchScrollableLazyColumn

/** GitHubのリンク */
private const val GITHUB_URL = "https://github.com/takusan23/SpeedWatch"

/** ツイッターリンク */
private const val TWITTER_URL = "https://twitter.com/takusan__23"

/** 設定画面 */
@Composable
fun SettingScreen(onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    val version = remember { context.packageManager.getPackageInfo(context.packageName, 0).versionName }
    val listState = rememberScalingLazyListState(initialCenterItemIndex = 0)

    Scaffold(
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) },
        timeText = {
            // スクロール中じゃない場合は出す
            if (!listState.isScrollInProgress) {
                TimeText()
            }
        }
    ) {
        WatchScrollableLazyColumn(
            modifier = Modifier.fillMaxWidth(),
            autoCentering = AutoCenteringParams(itemIndex = 0),
            state = listState,
        ) {
            item {
                ListHeader {
                    Text(text = stringResource(id = R.string.setting_screen_title))
                }
            }
            item {
                SettingItemChip(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(id = R.string.setting_screen_source_code_title)) },
                    secondaryLabel = { Text(text = stringResource(id = R.string.setting_screen_source_code_description)) },
                    onClick = {
                        // スマートフォンのブラウザを開く
                        openBrowserWithSmartphone(context, GITHUB_URL)
                    }
                )
            }
            item {
                SettingItemChip(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(id = R.string.setting_screen_version_title)) },
                    secondaryLabel = { Text(text = version) },
                    onClick = { /* do noting */ }
                )
            }
            item {
                SettingItemChip(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(id = R.string.setting_screen_twitter_title)) },
                    onClick = { openBrowserWithSmartphone(context, TWITTER_URL) }
                )
            }
            item {
                SettingItemChip(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(id = R.string.setting_screen_license_title)) },
                    secondaryLabel = { Text(text = stringResource(id = R.string.setting_screen_license_description)) },
                    onClick = { onNavigate(Navigates.LicenseScreen) }
                )
            }
        }
    }
}

/** スマホのブラウザを開く */
private fun openBrowserWithSmartphone(context: Context, url: String) {
    RemoteActivityHelper(context).startRemoteActivity(
        Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            addCategory(Intent.CATEGORY_BROWSABLE)
        }
    )
}

@Composable
private fun SettingItemChip(
    modifier: Modifier = Modifier,
    label: @Composable RowScope.() -> Unit,
    secondaryLabel: (@Composable RowScope.() -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    Chip(
        modifier = modifier,
        colors = ChipDefaults.chipColors(backgroundColor = MaterialTheme.colors.surface),
        label = label,
        secondaryLabel = secondaryLabel,
        onClick = onClick
    )
}