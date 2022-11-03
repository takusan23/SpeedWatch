package io.github.takusan23.speedwatch.ui.screen

import android.content.Intent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.wear.compose.material.*
import androidx.wear.remote.interactions.RemoteActivityHelper
import io.github.takusan23.speedwatch.Navigates
import io.github.takusan23.speedwatch.ui.component.WatchScrollableLazyColumn

/** GitHubのリンク */
private const val GITHUB_URL = "https://github.com/takusan23/SpeedWatch"

/** 設定画面 */
@Composable
fun SettingScreen(onNavigate: (String) -> Unit) {
    val context = LocalContext.current
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
                    Text(text = "設定")
                }
            }
            item {
                SettingItemChip(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "ソースコード") },
                    secondaryLabel = { Text(text = "スマートフォンで開く") },
                    onClick = {
                        // スマートフォンのブラウザを開く
                        RemoteActivityHelper(context).startRemoteActivity(
                            Intent(Intent.ACTION_VIEW, GITHUB_URL.toUri()).apply {
                                addCategory(Intent.CATEGORY_BROWSABLE)
                            }
                        )
                    }
                )
            }
            item {
                SettingItemChip(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "ライセンス") },
                    secondaryLabel = { Text(text = "助かります！") },
                    onClick = { onNavigate(Navigates.LicenseScreen) }
                )
            }
        }
    }
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