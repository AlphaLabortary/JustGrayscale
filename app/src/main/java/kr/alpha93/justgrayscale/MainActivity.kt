package kr.alpha93.justgrayscale

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import kr.alpha93.justgrayscale.ui.elements.ADBGuideDialog
import kr.alpha93.justgrayscale.ui.elements.PermissionSection
import kr.alpha93.justgrayscale.ui.elements.ServiceSection
import kr.alpha93.justgrayscale.ui.theme.Theme
import kr.alpha93.justgrayscale.ui.util.ResourcedText
import kr.alpha93.justgrayscale.ui.util.makeToast

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dialogState = mutableStateOf(false)

        if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            requestNotification()

        setContent {
            Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (dialogState.value) ADBGuideDialog(dialogState)

                    Scaffold(
                        topBar = { TopAppBar(title = { ResourcedText(R.string.app_name) }) }
                    ) { padding ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(15.dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    PaddingValues(
                                        10.dp,
                                        padding.calculateTopPadding(),
                                        10.dp,
                                        10.dp
                                    )
                                )
                        ) {
                            PermissionSection(dialogState)
                            // TODO: Add Battery Optimization Section
                            // TODO: Add Notification Section
                            ServiceSection()
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotification() {
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) return

        if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            makeToast(this, R.string.service_stopped_notification_denied)
            return
        }

        requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
    }
}
