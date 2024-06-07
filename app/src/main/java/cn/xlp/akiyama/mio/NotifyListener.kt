package cn.xlp.akiyama.mio

import android.service.notification.StatusBarNotification

interface NotifyListener {
    fun onReceiveMessage(sbn: StatusBarNotification)
    fun onRemovedMessage(sbn: StatusBarNotification)
}
