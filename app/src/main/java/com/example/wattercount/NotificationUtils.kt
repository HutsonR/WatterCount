//package com.example.wattercount
//
//import android.app.AlarmManager
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import android.provider.Settings
//import androidx.core.app.NotificationCompat
//import androidx.core.content.ContextCompat
//import java.util.*
//
//class NotificationUtils(private val context: Context) {
//
//    private val channelId = "water_notification_channel"
//    private val channelName = "Water Reminder"
//    private val notificationId = 1
//    private val alarmRequestCode = 100
//
//    fun scheduleNotification() {
//        // Создаем канал уведомлений (только для Android 8.0+)
//        createNotificationChannel()
//
//        // Создаем намерение для отправки в BroadcastReceiver
//        val intent = Intent(context, NotificationReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            alarmRequestCode,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        // Устанавливаем время первого уведомления (сейчас)
//        val calendar = Calendar.getInstance()
//        calendar.add(Calendar.HOUR_OF_DAY, 3)
//
//        // Получаем системный сервис AlarmManager
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//        // Планируем повторяющееся уведомление каждые 3 часа
//        alarmManager.setInexactRepeating(
//            AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis,
//            AlarmManager.INTERVAL_HOUR * 3,
//            pendingIntent
//        )
//    }
//
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                channelName,
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//
//            val notificationManager =
//                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
//
//    fun showNotification() {
//        val notificationBuilder = NotificationCompat.Builder(context, channelId)
//            .setSmallIcon(R.drawable.ic_circle_notification)
//            .setContentTitle("Напоминание о питье воды")
//            .setContentText("Пора пить воду!")
//            .setColor(ContextCompat.getColor(context, R.color.primary))
//            .setAutoCancel(true)
//
//        val notificationManager =
//            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
//            !notificationManager.isNotificationPolicyAccessGranted
//        ) {
//            // Запросите разрешение у пользователя для отправки уведомлений
//            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
//            context.startActivity(intent)
//        } else {
//            // Отправка уведомления
//            notificationManager.notify(notificationId, notificationBuilder.build())
//        }
//    }
//
//    class NotificationReceiver : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            val notificationUtils = NotificationUtils(context)
//            notificationUtils.showNotification()
//        }
//    }
//}
