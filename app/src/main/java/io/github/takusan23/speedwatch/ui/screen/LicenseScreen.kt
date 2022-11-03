package io.github.takusan23.speedwatch.ui.screen

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.*

/** ライセンス画面 */
@Composable
fun LicenseScreen() {
    val listState = rememberScalingLazyListState(initialCenterItemIndex = 0)
    val list = listOf(materialIcons)

    ScalingLazyColumn(state = listState) {
        item {
            ListHeader {
                Text(text = "ライセンス")
            }
        }
        items(list) { licenseData ->
            TitleCard(
                onClick = { /* do nothing */ },
                title = { Text(text = licenseData.name) },
                content = { Text(text = licenseData.license) }
            )
        }
    }
}

private val materialIcons = LicenseData(
    name = "google/material-design-icons",
    license = """
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
    """.trimIndent()
)

/**
 * ライセンス情報データクラス
 * @param name 名前
 * @param license ライセンス
 * */
private data class LicenseData(
    val name: String,
    val license: String,
)