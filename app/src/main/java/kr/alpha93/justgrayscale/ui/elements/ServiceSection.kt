package kr.alpha93.justgrayscale.ui.elements

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.alpha93.justgrayscale.R
import kr.alpha93.justgrayscale.services.BackgroundService
import kr.alpha93.justgrayscale.ui.theme.Theme
import kr.alpha93.justgrayscale.ui.util.ResourcedIcon
import kr.alpha93.justgrayscale.ui.util.ResourcedText
import kr.alpha93.justgrayscale.ui.util.makeToast

val isRegistered = mutableStateOf(false)

fun startService(context: Context) {
    Intent(context, BackgroundService::class.java).run {
        context.startService(this)
    }
    makeToast(context, R.string.service_started, Toast.LENGTH_SHORT)
}

fun checkService(context: Context) {
    isRegistered.value = (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getRunningServices(Int.MAX_VALUE)
        .any { it.service.className == "kr.alpha93.justgrayscale.services.BackgroundService" }
}

fun checkBatteryOptimizations(context: Context): Boolean {
    val state = (context.getSystemService(ComponentActivity.POWER_SERVICE) as PowerManager)
        .isIgnoringBatteryOptimizations(context.packageName)
    return state
}

@SuppressLint("BatteryLife")
fun requestBatteryOptimizationsAndStart(context: Context) {
    Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).also {
        it.data = android.net.Uri.parse("package:${context.packageName}")
        context.startActivity(it)
    }
}

@Composable
fun ServiceSection() {
    checkService(LocalContext.current)
    if (isRegistered.value) ServiceRegisteredSection() else ServiceStoppedSection()
}

@Composable
@Preview(device = "id:pixel_8_pro")
private fun ServiceRegisteredSection() {
    Theme {
        Card(
            modifier = Modifier.fillMaxWidth(),
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
                    alt = R.string.service_started,
                    tint = Color(0xFF1B5E20),
                    modifier = Modifier
                        .background(Color(0xFF81C784), CircleShape)
                        .padding(10.dp)
                        .size(30.dp)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    ResourcedText(R.string.service, fontSize = 20.sp)
                    ResourcedText(R.string.service_started_content)
                }
            }
        }
    }
}


@Composable
@Preview(device = "id:pixel_8_pro")
private fun ServiceStoppedSection() {
    val context = LocalContext.current

    Theme {
        Card(
            modifier = Modifier.fillMaxWidth(),
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
                        alt = R.string.service_stopped,
                        tint = Color(0xFFF9A825),
                        modifier = Modifier
                            .background(Color(0xFFFFF176), CircleShape)
                            .padding(10.dp)
                            .size(30.dp)
                    )
                    ResourcedText(R.string.service, fontSize = 20.sp)
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ResourcedText(R.string.service_stopped_content)
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                            Button(
                                onClick = {
                                    if (!checkBatteryOptimizations(context)) {
                                        requestBatteryOptimizationsAndStart(context)
                                        makeToast(context, R.string.permission_grant_via_shizuku_not_running, Toast.LENGTH_SHORT)
                                    } else {
                                        startService(context)
                                        Log.i("button", "started")
                                    }
                                },
                                content = { ResourcedText(R.string.service_start) }
                            )
                        }
                    }
                }
            }
        }
    }
}
