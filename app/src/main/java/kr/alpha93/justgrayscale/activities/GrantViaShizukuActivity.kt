package kr.alpha93.justgrayscale.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kr.alpha93.justgrayscale.R
import kr.alpha93.justgrayscale.ui.elements.canStart
import kr.alpha93.justgrayscale.ui.util.makeToast
import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuRemoteProcess
import java.io.BufferedInputStream

class GrantViaShizukuActivity : Activity() {

    private val requestCode = 0
    private val listener = Shizuku.OnRequestPermissionResultListener { _, grantResult ->
        if (grantResult == PackageManager.PERMISSION_GRANTED) {
            listOf(
                "pm grant $packageName android.permission.WRITE_SECURE_SETTINGS",
                "pm grant $packageName android.permission.POST_NOTIFICATIONS",
                "dumpsys deviceidle whitelist +$packageName"
            ).forEach {
                val process: ShizukuRemoteProcess = Shizuku.newProcess(
                    arrayOf(
                        "sh",
                        "-c",
                        it,
                    ), null, "/"
                )
                Log.d(
                    "Shizuku",
                    BufferedInputStream(process.inputStream).bufferedReader().readText()
                )
                process.waitFor()
            }

            if (canStart(this)) makeToast(this, R.string.permission_granted)

        } else if (Shizuku.shouldShowRequestPermissionRationale())
            makeToast(this, R.string.permission_grant_shizuku_user_denied)
        else
            makeToast(this, R.string.permission_grant_shizuku_failed)

        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Shizuku.addRequestPermissionResultListener(listener)
        requestPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        Shizuku.removeRequestPermissionResultListener(listener)
    }

    @SuppressLint("WrongConstant")
    private fun requestPermission() {
        try {
            this.packageManager.getPackageInfo("moe.shizuku.privileged.api", 0)
        } catch (e: PackageManager.NameNotFoundException) {
            makeToast(this, R.string.permission_grant_shizuku_notfound, Toast.LENGTH_SHORT)
            finish()
            return
        }

        if (Shizuku.isPreV11()) {
            makeToast(this, R.string.permission_grant_shizuku_outdated, Toast.LENGTH_SHORT)
            finish()
            return
        }

        if (!Shizuku.pingBinder()) {
            makeToast(this, R.string.permission_grant_shizuku_interrupted, Toast.LENGTH_SHORT)
            finish()
            return
        }

        if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            Shizuku.requestPermission(requestCode)
            return
        }

        listener.onRequestPermissionResult(requestCode, PackageManager.PERMISSION_GRANTED)
    }

}
