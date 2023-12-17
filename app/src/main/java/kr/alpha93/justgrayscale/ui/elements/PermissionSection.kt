package kr.alpha93.justgrayscale.ui.elements

import android.Manifest
import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.alpha93.justgrayscale.R
import kr.alpha93.justgrayscale.activities.GrantViaShizukuActivity
import kr.alpha93.justgrayscale.ui.theme.Theme
import kr.alpha93.justgrayscale.ui.util.ResourcedIcon
import kr.alpha93.justgrayscale.ui.util.ResourcedText

private val permissionStatus = mutableStateOf(false)

fun checkPermission(context: Context): Boolean {
    permissionStatus.value =
        context.checkSelfPermission(Manifest.permission.WRITE_SECURE_SETTINGS) == PackageManager.PERMISSION_GRANTED
    return permissionStatus.value
}

@Composable
fun PermissionSection(
    dialogState: MutableState<Boolean>
) {
    checkPermission(LocalContext.current)

    if (permissionStatus.value)
        PermissionSectionGranted()
    else
        PermissionSectionRevoked(dialogState)
}

@Composable
@Preview(device = "id:pixel_8_pro")
private fun PermissionSectionGranted() {
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
                    alt = R.string.permission_granted,
                    tint = Color(0xFF1B5E20),
                    modifier = Modifier
                        .background(Color(0xFF81C784), CircleShape)
                        .padding(10.dp)
                        .size(30.dp)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    ResourcedText(R.string.permission, fontSize = 20.sp)
                    ResourcedText(R.string.permission_granted_content)
                }
            }
        }
    }
}

@Composable
@Preview(device = "id:pixel_8_pro")
private fun PermissionSectionRevoked(
    dialogState: MutableState<Boolean> = mutableStateOf(false)
) {
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
                        alt = R.string.permission_revoked,
                        tint = Color(0xFFB71C1C),
                        modifier = Modifier
                            .background(Color(0xFFE57373), CircleShape)
                            .padding(10.dp)
                            .size(30.dp)
                    )
                    ResourcedText(R.string.permission, fontSize = 20.sp)
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ResourcedText(R.string.permission_revoked_content)
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                            Button(
                                onClick = { dialogState.value = true },
                                content = { ResourcedText(R.string.permission_revoked_adb) }
                            )
                            Button(
                                onClick = {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            GrantViaShizukuActivity::class.java
                                        )
                                    )
                                },
                                content = { ResourcedText(R.string.permission_revoked_shizuku) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(device = "id:pixel_8_pro")
fun ADBGuideDialog(
    dialogState: MutableState<Boolean> = mutableStateOf(false)
) {
    val manager: ClipboardManager =
        LocalContext.current.getSystemService(Service.CLIPBOARD_SERVICE) as ClipboardManager
    val clipdata = ClipData.newPlainText(
        "ADB Command",
        "adb shell pm grant kr.alpha93.justgrayscale android.permission.WRITE_SECURE_SETTINGS"
    )

    Theme {
        AlertDialog(
            title = { ResourcedText(R.string.permission_grant_via_adb) },
            text = {
                Column {
                    ResourcedText(R.string.permission_grant_via_adb_content)
                    ClickableText(
                        text = AnnotatedString("adb shell pm grant kr.alpha93.justgrayscale android.permission.WRITE_SECURE_SETTINGS"),
                        onClick = { manager.setPrimaryClip(clipdata) },
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            lineBreak = LineBreak.Paragraph
                        ),
                        modifier = Modifier
                            .padding(20.dp, 20.dp, 20.dp, 0.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                }
            },
            confirmButton = {
                Button(
                    content = { ResourcedText(R.string.close) },
                    onClick = { dialogState.value = false }
                )
            },
            dismissButton = {
                Button(
                    content = { ResourcedText(R.string.copy) },
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.secondaryContainer,
                        MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    onClick = { manager.setPrimaryClip(clipdata); dialogState.value = false }
                )
            },
            onDismissRequest = {}
        )
    }
}
