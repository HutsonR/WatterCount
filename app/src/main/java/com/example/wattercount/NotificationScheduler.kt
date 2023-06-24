//package com.example.wattercount
//
//import android.app.AlarmManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import java.util.*
//
//class NotificationScheduler {
//    companion object {
//        private const val NOTIFICATION_REQUEST_CODE = 1001
//
//        fun scheduleNotification(context: Context) {
//            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//            val intent = Intent(context, NotificationReceiver::class.java)
//            val pendingIntent = PendingIntent.getBroadcast(
//                context,
//                NOTIFICATION_REQUEST_CODE,
//                intent,
//                PendingIntent.FLAG_UPDATE_CURRENT
//            )
//
//            val calendar = Calendar.getInstance()
//            calendar.add(Calendar.HOUR_OF_DAY, 3)
//
//            alarmManager.setRepeating(
//                AlarmManager.RTC_WAKEUP,
//                calendar.timeInMillis,
//                AlarmManager.INTERVAL_HOUR * 3,
//                pendingIntent
//            )
//        }
//
//        fun cancelNotification(context: Context) {
//            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//            val intent = Intent(context, NotificationReceiver::class.java)
//            val pendingIntent = PendingIntent.getBroadcast(
//                context,
//                NOTIFICATION_REQUEST_CODE,
//                intent,
//                PendingIntent.FLAG_UPDATE_CURRENT
//            )
//
//            alarmManager.cancel(pendingIntent)
//            pendingIntent.cancel()
//        }
//    }
//}
