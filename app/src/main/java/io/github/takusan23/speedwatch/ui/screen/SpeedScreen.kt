package io.github.takusan23.speedwatch.ui.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Looper
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.wear.compose.foundation.CurvedTextStyle
import androidx.wear.compose.material.*
import com.google.android.gms.location.*
import io.github.takusan23.speedwatch.Navigates
import io.github.takusan23.speedwatch.R
import io.github.takusan23.speedwatch.data.LocationData
import io.github.takusan23.speedwatch.tool.ActivityTool
import io.github.takusan23.speedwatch.tool.HaversineFunction
import io.github.takusan23.speedwatch.ui.component.WatchScrollableLazyColumn

/** 位置情報の定期取得する間隔（ミリ秒） */
private const val IntervalMs = 5_000L

/**
 * メイン画面
 *
 * @param onNavigate 画面切り替え時に呼ばれる
 */
@Composable
@SuppressLint("MissingPermission")
fun SpeedScreen(onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val listState = rememberScalingLazyListState(initialCenterItemIndex = 0)
    // 常時点灯させるか
    val isAOD = remember { mutableStateOf(false) }
    // 前回の位置
    val currentLocationData = remember { mutableStateOf<LocationData?>(null) }
    // 時速をだす
    val speedKmHour = remember { mutableStateOf(0.0) }

    // 位置情報を発行する
    val locationService = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationCallback = remember {
        object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val lastLocation = locationResult.lastLocation ?: return
                val latestData = LocationData(lastLocation.latitude, lastLocation.longitude)
                if (currentLocationData.value != null) {
                    // 差分を出す
                    val location1 = latestData
                    val location2 = currentLocationData.value!!
                    // 単位はキローメートル
                    val diffKm = HaversineFunction.calc(location1.latitude, location1.longitude, location2.latitude, location2.longitude)
                    // 時速を出す
                    // 前回からどれぐらい時間がたったか（秒）
                    val diffSec = (location1.time - location2.time) / 1000
                    // 1秒あたりの距離を出して、時間 ( 3600 ) をかける
                    speedKmHour.value = (diffKm / diffSec) * 3600
                }
                currentLocationData.value = latestData
            }

        }
    }

    // ライフサイクルを監視して、表示されているときのみ位置情報を補足する
    LaunchedEffect(key1 = Unit) {
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                // リセットする
                currentLocationData.value = null
                speedKmHour.value = 0.0
                // 位置情報を購読
                val locationRequest = LocationRequest.Builder(IntervalMs).apply {
                    setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    setWaitForAccurateLocation(true)
                }.build()
                locationService.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            }

            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                locationService.removeLocationUpdates(locationCallback)
            }
        })
    }

    Scaffold(
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) },
        timeText = {
            // スクロール中じゃない場合は出す
            if (!listState.isScrollInProgress) {
                // 常時点灯 を表示
                val style = TimeTextDefaults.timeTextStyle()
                TimeText(
                    startCurvedContent = if (isAOD.value) {
                        {
                            curvedText(
                                text = context.getString(R.string.speed_screen_aod),
                                style = CurvedTextStyle(style)
                            )
                        }
                    } else null,
                    startLinearContent = if (isAOD.value) {
                        {
                            Text(
                                text = context.getString(R.string.speed_screen_aod),
                                style = style
                            )
                        }
                    } else null,
                )
            }
        }
    ) {
        WatchScrollableLazyColumn(
            autoCentering = AutoCenteringParams(itemIndex = 0),
            state = listState,
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.2f), // 正方形からちょいずらす
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        // 時速 3.00 キロ みたいな感じになる
                        text = stringResource(id = R.string.speed_screen_speed_template).format(speedKmHour.value),
                        fontSize = 24.sp
                    )
                }
            }
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) { Icon(painter = painterResource(id = R.drawable.expand_more_24px), contentDescription = null) }
            }
            item {
                ToggleChip(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(id = R.string.speed_screen_setting_aod_title)) },
                    secondaryLabel = { Text(text = stringResource(id = R.string.speed_screen_setting_aod_description)) },
                    checked = isAOD.value,
                    onCheckedChange = {
                        // Activityをスリープしないように
                        isAOD.value = it
                        ActivityTool.setSleepBlock((context as Activity), it)
                    },
                    toggleControl = {
                        Icon(
                            modifier = Modifier.size(ToggleChipDefaults.IconSize),
                            imageVector = ToggleChipDefaults.switchIcon(checked = isAOD.value),
                            contentDescription = null
                        )
                    }
                )
            }
            item {
                Chip(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ChipDefaults.chipColors(backgroundColor = MaterialTheme.colors.surface),
                    label = { Text(text = currentLocationData.value?.timeFormat ?: "") },
                    secondaryLabel = { Text(text = stringResource(id = R.string.speed_screen_setting_latest_update_title)) },
                    onClick = { /* do nothing */ }
                )
            }
            item {
                Chip(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ChipDefaults.chipColors(backgroundColor = MaterialTheme.colors.surface),
                    label = { Text(text = stringResource(id = R.string.speed_screen_setting_setting)) },
                    onClick = { onNavigate(Navigates.SettingScreen) }
                )
            }
        }
    }
}
