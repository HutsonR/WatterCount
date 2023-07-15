package com.example.wattercount

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationUtils = NotificationUtils(context)
        notificationUtils.showNotification()
    }
}