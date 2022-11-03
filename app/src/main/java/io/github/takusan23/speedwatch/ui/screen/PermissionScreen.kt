package io.github.takusan23.speedwatch.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import io.github.takusan23.speedwatch.R

@Composable
fun PermissionScreen(onGrant: () -> Unit) {
    val permissionRequest = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) {
        if (it.all { it.value }) {
            onGrant()
        }
    }
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(horizontal = 10.dp)
    ) {
        item {
            ListHeader {
                Text(text = "お願い")
            }
        }
        item {
            Text(
                text = "位置情報の権限が必要です",
                textAlign = TextAlign.Center
            )
        }
        item {
            Button(
                onClick = {
                    permissionRequest.launch(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION))
                }
            ) { Icon(painter = painterResource(id = R.drawable.arrow_forward_24px), contentDescription = null) }
        }
    }
}