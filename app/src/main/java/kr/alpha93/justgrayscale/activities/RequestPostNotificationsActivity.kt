package kr.alpha93.justgrayscale.activities

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import kr.alpha93.justgrayscale.R
import kr.alpha93.justgrayscale.ui.elements.checkNotification
import kr.alpha93.justgrayscale.ui.util.makeToast

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class RequestPostNotificationsActivity : Activity() {

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            makeToast(this, R.string.notification_allowed)
        else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS))
            makeToast(this, R.string.notification_denied_user)
        else makeToast(this, R.string.notification_denied_unknown)

        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkNotification(this)) {
            finish()
            return
        }

        requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
    }

}
