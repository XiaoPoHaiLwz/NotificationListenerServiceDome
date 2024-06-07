package cn.xlp.akiyama.mio

import android.service.notification.StatusBarNotification

class NotifyHelper private constructor() {

    companion object {

        @Volatile
        private var instance: NotifyHelper? = null

        fun getInstance(): NotifyHelper {
            return instance ?: synchronized(this) {
                instance ?: NotifyHelper().also { instance = it }
            }
        }
    }

    private var notifyListener: NotifyListener? = null

    fun setNotifyListener(notifyListener: NotifyListener?) {
        this.notifyListener = notifyListener
    }

    fun onReceive(sbn: StatusBarNotification) {
        notifyListener?.onReceiveMessage(sbn)
    }

    fun onRemoved(sbn: StatusBarNotification) {
        notifyListener?.onRemovedMessage(sbn)
    }
}
