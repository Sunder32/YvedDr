package com.example.yveddr.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.yveddr.model.Friend
import java.time.LocalDate
import java.util.*

class NotificationReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        val notificationService = NotificationService(context)
        
        when (intent.action) {
            "BIRTHDAY_NOTIFICATION" -> {
                val friendId = intent.getIntExtra("friend_id", 0)
                val friendName = intent.getStringExtra("friend_name") ?: ""
                val friendAge = intent.getIntExtra("friend_age", 0)
                
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.YEAR, -friendAge)
                val birthday = LocalDate.of(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                
                val friend = Friend(
                    id = friendId,
                    name = friendName,
                    birthday = birthday
                )
                
                notificationService.sendBirthdayNotification(friend)
            }
            
            "UPCOMING_BIRTHDAY_NOTIFICATION" -> {
                val friendId = intent.getIntExtra("friend_id", 0)
                val friendName = intent.getStringExtra("friend_name") ?: ""
                val daysLeft = intent.getLongExtra("days_left", 0)
                
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_MONTH, daysLeft.toInt())
                val birthday = LocalDate.of(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                
                val friend = Friend(
                    id = friendId,
                    name = friendName,
                    birthday = birthday
                )
                
                notificationService.sendUpcomingBirthdayNotification(friend, daysLeft)
            }
        }
    }
}