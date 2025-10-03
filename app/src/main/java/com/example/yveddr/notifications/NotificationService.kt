package com.example.yveddr.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.yveddr.MainActivity
import com.example.yveddr.R
import com.example.yveddr.model.Friend

class NotificationService(private val context: Context) {
    
    companion object {
        const val CHANNEL_ID = "birthday_notifications"
        const val CHANNEL_NAME = "Birthday Notifications"
        const val CHANNEL_DESCRIPTION = "Уведомления о днях рождения друзей"
    }
    
    init {
        createNotificationChannel()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableLights(true)
                enableVibration(true)
            }
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    fun sendBirthdayNotification(friend: Friend) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, 
            0, 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.logo))
            .setContentTitle("🎉 День Рождения!")
            .setContentText("Сегодня день рождения у ${friend.name}! 🎂")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("🎀 Hello Kitty напоминает: сегодня день рождения у ${friend.name}! Исполняется ${friend.getAge()} лет. Не забудьте поздравить! 🎂💕")
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setColor(0xFFE91E63.toInt()) // Hello Kitty pink
            .build()
        
        try {
            NotificationManagerCompat.from(context).notify(friend.id, notification)
        } catch (e: SecurityException) {
        }
    }
    
    fun sendTestNotification() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, 
            0, 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("🎀 Тестовое уведомление!")
            .setContentText("Hello Kitty уведомления работают! 💕")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("🎉 Отлично! Система уведомлений работает правильно! Теперь вы не пропустите ни одного дня рождения! 🎂💕🎀")
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setColor(0xFFE91E63.toInt())
            .build()
        
        try {
            NotificationManagerCompat.from(context).notify(999999, notification)
        } catch (e: SecurityException) {
            android.util.Log.e("NotificationService", "Permission not granted: ${e.message}")
        }
    }
    
    fun sendUpcomingBirthdayNotification(friend: Friend, daysLeft: Long) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, 
            friend.id + 1000, 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("🗓️ Скоро день рождения!")
            .setContentText("У ${friend.name} через $daysLeft ${getDaysText(daysLeft)} день рождения!")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("🎀 Hello Kitty напоминает: у ${friend.name} через $daysLeft ${getDaysText(daysLeft)} день рождения! Подготовьте поздравление заранее! 💕")
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setColor(0xFFE91E63.toInt())
            .build()
        
        try {
            NotificationManagerCompat.from(context).notify(friend.id + 1000, notification)
        } catch (e: SecurityException) {
        }
    }
    
    private fun getDaysText(days: Long): String {
        return when {
            days == 1L -> "день"
            days in 2..4 -> "дня"
            else -> "дней"
        }
    }
}