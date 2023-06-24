//package com.example.wattercount
//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//
//class NotificationReceiver : BroadcastReceiver() {
//    companion object {
//        private const val CHANNEL_ID = "NotificationChannel"
//        private const val NOTIFICATION_ID = 1
//    }
//
//    override fun onReceive(context: Context, intent: Intent) {
//        createNotificationChannel(context)
//
//        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
//            .setContentTitle("Напоминание")
//            .setContentText("Пора пить воду!")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setAutoCancel(true)
//
//        val notificationManager = NotificationManagerCompat.from(context)
//        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
//    }
//
//    private fun createNotificationChannel(context: Context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = "NotificationChannelName"
//            val descriptionText = "NotificationChannelDescription"
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
//                description = descriptionText
//            }
//            val notificationManager =
//                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
//}
