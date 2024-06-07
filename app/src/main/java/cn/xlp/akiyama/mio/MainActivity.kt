package cn.xlp.akiyama.mio

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.service.notification.StatusBarNotification
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity(), NotifyListener {
    companion object {
        private const val REQUEST_CODE = 9527
        @SuppressLint("StaticFieldLeak")
        private lateinit var textView: TextView
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textView)
        NotifyHelper.getInstance().setNotifyListener(this)
        startService(Intent(this@MainActivity, NotifyService::class.java))
    }
    private fun isNLServiceEnabled(): Boolean {
        val packageNames = NotificationManagerCompat.getEnabledListenerPackages(this)
        return packageNames.contains(packageName)
    }

    private fun toggleNotificationListenerService() {
        val pm = packageManager
        val componentName = ComponentName(applicationContext, NotifyService::class.java)

        pm.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )

        pm.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun showMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    @Suppress("DEPRECATION")
    fun requestPermission(view: View) {
        if (!isNLServiceEnabled()) {
            startActivityForResult(
                Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"),
                REQUEST_CODE
            )
        } else {
            showMsg("通知服务已开启")
            toggleNotificationListenerService()
        }
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        showMsg("通知服务已开启")
        toggleNotificationListenerService()
    }

    override fun onReceiveMessage(sbn: StatusBarNotification) {
        if (sbn.notification == null) return

        var msgContent = ""
        sbn.notification.tickerText?.let {
            msgContent = it.toString()
        }
        val time =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE).format(Date(sbn.postTime))
        textView.text = String.format(
            Locale.getDefault(),
            "应用包名：%s\n消息内容：%s\n消息时间：%s\n",
            sbn.packageName, msgContent, time
        )
        showMsg(msgContent)
    }

    override fun onRemovedMessage(sbn: StatusBarNotification) {
        textView.text = "通知移除"
    }
}
