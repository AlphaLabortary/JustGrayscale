package kr.alpha93.justgrayscale.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build.VERSION
import android.os.IBinder
import kr.alpha93.justgrayscale.R
import kr.alpha93.justgrayscale.receiver.ScreenReceiver
import kr.alpha93.justgrayscale.ui.elements.checkService

class BackgroundService : Service() {

    private val serviceConnection = BackgroundService.ServiceConnection()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        bindService(Intent(this, BackgroundService::class.java), serviceConnection, BIND_AUTO_CREATE)
        checkService(this)

        val receiver: BroadcastReceiver = ScreenReceiver()

        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)

        registerReceiver(receiver, filter)
        notificate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun notificate() {
        val intent = Intent(this, BackgroundService::class.java)

        val builder: Notification.Builder = Notification.Builder(this, "default").also {
            it.setSmallIcon(android.R.drawable.ic_dialog_info)
            it.setContentTitle(getText(R.string.app_name))
            it.setContentText(getText(R.string.service_running))
            it.setContentIntent(PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE))
        }

        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).run {
            createNotificationChannel(
                NotificationChannel("service", "Foreground Service",
                    NotificationManager.IMPORTANCE_DEFAULT)
            )

            if (VERSION.SDK_INT >= 29)
                startForeground(1, builder.build())
            else
                notify(1, builder.build())
        }
    }

    class BackgroundBinder : Binder() {
        fun getService(): BackgroundService {
            return BackgroundService()
        }
    }

    class ServiceConnection : android.content.ServiceConnection {
        private var mService: BackgroundService? = null;

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mService = (service as BackgroundBinder).getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mService = null
        }

        fun isServiceRunning(): Boolean {
            return mService != null
        }
    }

}
