package kr.alpha93.justgrayscale.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings.Secure

class ScreenReceiver : BroadcastReceiver() {

    private val displayDaltonizerStatus: String = "accessibility_display_daltonizer_enabled"
    private val displayDaltonizerMode: String = "accessibility_display_daltonizer"

    private var defaultDaltonizerStatus: Int = 0
    private var defaultDaltonizerMode: Int = -1

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_SCREEN_OFF -> {
                defaultDaltonizerStatus =
                    Secure.getInt(context?.contentResolver, displayDaltonizerStatus)
                defaultDaltonizerMode =
                    Secure.getInt(context?.contentResolver, displayDaltonizerMode)
                Secure.putInt(context?.contentResolver, displayDaltonizerStatus, 1)
                Secure.putInt(context?.contentResolver, displayDaltonizerMode, 0)
            }

            Intent.ACTION_SCREEN_ON -> {
                Secure.putInt(
                    context?.contentResolver,
                    displayDaltonizerStatus,
                    defaultDaltonizerStatus
                )
                Secure.putInt(
                    context?.contentResolver,
                    displayDaltonizerMode,
                    defaultDaltonizerMode
                )
            }

            else -> return
        }
    }

}
