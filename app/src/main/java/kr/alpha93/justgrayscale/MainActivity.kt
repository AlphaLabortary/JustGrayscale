package kr.alpha93.justgrayscale

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.alpha93.justgrayscale.ui.elements.ADBGuideDialog
import kr.alpha93.justgrayscale.ui.elements.DozeSettingsSection
import kr.alpha93.justgrayscale.ui.elements.NotificationSection
import kr.alpha93.justgrayscale.ui.elements.SecureSettingsSection
import kr.alpha93.justgrayscale.ui.elements.ServiceSection
import kr.alpha93.justgrayscale.ui.theme.Theme
import kr.alpha93.justgrayscale.ui.util.ResourcedText

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNotificationsChannel()

        val dialogState = mutableStateOf(false)

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
                        LazyColumn(
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
                            item {
                                SecureSettingsSection(
                                    Modifier.animateItemPlacement(),
                                    dialogState
                                )
                            }
                            item { DozeSettingsSection(Modifier.animateItemPlacement()) }
                            item { NotificationSection(Modifier.animateItemPlacement()) }
                            item { ServiceSection(Modifier.animateItemPlacement()) }
                        }
                    }
                }
            }
        }
    }

    private fun setupNotificationsChannel() {
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).run {
            createNotificationChannelGroup(
                NotificationChannelGroup(
                    "miscellaneous",
                    getText(R.string.notification_group_misc).toString()
                ).also {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                        it.description =
                            getText(R.string.notification_group_misc_content).toString()
                }
            )

            createNotificationChannel(
                NotificationChannel(
                    "background",
                    getText(R.string.service),
                    NotificationManager.IMPORTANCE_MIN
                )
                    .also {
                        it.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                        it.description =
                            getText(R.string.notification_channel_content).toString()
                        it.group = "miscellaneous"
                    }
            )
        }
    }
}
