package kr.alpha93.justgrayscale.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import androidx.compose.runtime.mutableStateOf
import kr.alpha93.justgrayscale.receiver.ScreenReceiver
import kr.alpha93.justgrayscale.ui.elements.checkService

class BackgroundService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        checkService(this)

        val receiver: BroadcastReceiver = ScreenReceiver()

        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)

        this.registerReceiver(receiver, filter)
    }

}
