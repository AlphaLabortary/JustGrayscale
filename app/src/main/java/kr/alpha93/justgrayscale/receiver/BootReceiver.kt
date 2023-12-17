package kr.alpha93.justgrayscale.receiver

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import kr.alpha93.justgrayscale.ui.elements.startService

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (!intent?.action.equals(Intent.ACTION_BOOT_COMPLETED)) return
        if (context != null) {
            if (context.checkSelfPermission(Manifest.permission.WRITE_SECURE_SETTINGS) != PackageManager.PERMISSION_GRANTED)
                return

            startService(context)
        }
    }

}