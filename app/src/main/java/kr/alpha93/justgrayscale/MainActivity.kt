package kr.alpha93.justgrayscale

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import kr.alpha93.justgrayscale.ui.elements.ADBGuideDialog
import kr.alpha93.justgrayscale.ui.elements.PermissionSection
import kr.alpha93.justgrayscale.ui.elements.ServiceSection
import kr.alpha93.justgrayscale.ui.theme.Theme
import kr.alpha93.justgrayscale.ui.util.ResourcedText

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                            ServiceSection()
                        }
                    }
                }
            }
        }
    }
}
