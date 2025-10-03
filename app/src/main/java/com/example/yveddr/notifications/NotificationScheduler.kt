package com.example.yveddr.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.yveddr.model.Friend
import java.time.LocalDate
import java.util.*

class NotificationScheduler(private val context: Context) {
    
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    
    fun scheduleBirthdayNotifications(friends: List<Friend>) {
        friends.forEach { friend ->
            scheduleBirthdayNotification(friend)
            scheduleUpcomingNotification(friend)
        }
    }
    
    private fun scheduleBirthdayNotification(friend: Friend) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = "BIRTHDAY_NOTIFICATION"
            putExtra("friend_id", friend.id)
            putExtra("friend_name", friend.name)
            putExtra("friend_age", friend.getAge())
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            friend.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val today = Calendar.getInstance()
        val currentYear = today.get(Calendar.YEAR)
        val currentMonth = today.get(Calendar.MONTH) + 1 // Calendar месяцы начинаются с 0
        val currentDay = today.get(Calendar.DAY_OF_MONTH)
        
        val birthdayCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, currentYear)
            set(Calendar.MONTH, friend.birthday.monthValue - 1) // LocalDate месяцы начинаются с 1
            set(Calendar.DAY_OF_MONTH, friend.birthday.dayOfMonth)
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        
        if (birthdayCalendar.timeInMillis <= System.currentTimeMillis()) {
            birthdayCalendar.set(Calendar.YEAR, currentYear + 1)
        }
        
        val triggerTime = birthdayCalendar.timeInMillis
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
    }
    
    private fun scheduleUpcomingNotification(friend: Friend) {
        val daysUntil = friend.getDaysUntilBirthday()
        
        if (daysUntil > 3) {
            val intent = Intent(context, NotificationReceiver::class.java).apply {
                action = "UPCOMING_BIRTHDAY_NOTIFICATION"
                putExtra("friend_id", friend.id)
                putExtra("friend_name", friend.name)
                putExtra("days_left", 3L)
            }
            
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                friend.id + 1000,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            val today = Calendar.getInstance()
            val currentYear = today.get(Calendar.YEAR)
            
            val birthdayCalendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, currentYear)
                set(Calendar.MONTH, friend.birthday.monthValue - 1)
                set(Calendar.DAY_OF_MONTH, friend.birthday.dayOfMonth)
            }
            
            if (birthdayCalendar.timeInMillis < System.currentTimeMillis()) {
                birthdayCalendar.set(Calendar.YEAR, currentYear + 1)
            }
            
            val reminderCalendar = Calendar.getInstance().apply {
                timeInMillis = birthdayCalendar.timeInMillis
                add(Calendar.DAY_OF_MONTH, -3)
                set(Calendar.HOUR_OF_DAY, 10)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            
            val triggerTime = reminderCalendar.timeInMillis
            
            if (triggerTime > System.currentTimeMillis()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                } else {
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                }
            }
        }
    }
    
    fun cancelAllNotifications(friends: List<Friend>) {
        friends.forEach { friend ->
            cancelBirthdayNotification(friend.id)
            cancelUpcomingNotification(friend.id)
        }
    }
    
    private fun cancelBirthdayNotification(friendId: Int) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            friendId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
    
    private fun cancelUpcomingNotification(friendId: Int) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            friendId + 1000,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}