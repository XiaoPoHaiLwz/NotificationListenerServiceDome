package cn.xlp.akiyama.mio

import android.content.ComponentName
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotifyService : NotificationListenerService(){
    companion object {
        const val TAG = "Akiyama_Mio"
        const val QQ = "com.tencent.mobileqq"
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        when (sbn.packageName) {
            QQ -> {
                Log.d(TAG, "收到信息拉")
                NotifyHelper.getInstance().onReceive(sbn)
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        when (sbn.packageName) {
            QQ -> {
                Log.d(TAG, "移除QQ消息")
                NotifyHelper.getInstance().onRemoved(sbn)
            }
        }
    }

    override fun onListenerDisconnected() {
        requestRebind(ComponentName(this, NotificationListenerService::class.java))
    }
}