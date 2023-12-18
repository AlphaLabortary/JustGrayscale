package kr.alpha93.justgrayscale.ui.elements

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.alpha93.justgrayscale.R
import kr.alpha93.justgrayscale.ui.theme.Theme
import kr.alpha93.justgrayscale.ui.util.ResourcedIcon
import kr.alpha93.justgrayscale.ui.util.ResourcedText

private val settingsStatus = mutableStateOf(false)

fun checkDozeSettings(context: Context): Boolean {
    settingsStatus.value = (context.getSystemService(ComponentActivity.POWER_SERVICE) as PowerManager)
        .isIgnoringBatteryOptimizations(context.packageName)
    return settingsStatus.value
}

@Composable
fun DozeSettingsSection(modifier: Modifier = Modifier) {
    val innerModifier = modifier.animateContentSize()
    if (checkDozeSettings(LocalContext.current)) ExcludeDoze(innerModifier) else IncludeDoze(innerModifier)
}

@Composable
@Preview(device = "id:pixel_8_pro")
private fun ExcludeDoze(modifier: Modifier = Modifier) {
    Theme {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(30.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                ResourcedIcon(
                    vec = Icons.Outlined.CheckCircle,
                    alt = R.string.battery_optimization_excluded,
                    tint = Color(0xFF1B5E20),
                    modifier = Modifier
                        .background(Color(0xFF81C784), CircleShape)
                        .padding(10.dp)
                        .size(30.dp)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    ResourcedText(R.string.battery_optimization, fontSize = 20.sp)
                    ResourcedText(R.string.battery_optimization_excluded_content)
                }
            }
        }
    }
}

@SuppressLint("BatteryLife")
private fun request(context: Context) {
    if (checkDozeSettings(context)) return
    Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).also {
        it.data = android.net.Uri.parse("package:${context.packageName}")
        context.startActivity(it)
    }
}

@Composable
@Preview(device = "id:pixel_8_pro")
private fun IncludeDoze(
    modifier: Modifier = Modifier,
    dialogState: MutableState<Boolean> = mutableStateOf(false)
) {
    val context = LocalContext.current

    Theme {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(30.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    ResourcedIcon(
                        vec = Icons.Outlined.Warning,
                        alt = R.string.battery_optimization_included,
                        tint = Color(0xFFB71C1C),
                        modifier = Modifier
                            .background(Color(0xFFE57373), CircleShape)
                            .padding(10.dp)
                            .size(30.dp)
                    )
                    ResourcedText(R.string.battery_optimization, fontSize = 20.sp)
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ResourcedText(R.string.battery_optimization_included_content)
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                            Button(
                                onClick = { request(context) },
                                content = { ResourcedText(R.string.battery_optimization_included_button) }
                            )
                        }
                    }
                }
            }
        }
    }
}
