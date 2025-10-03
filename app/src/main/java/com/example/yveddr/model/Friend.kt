package com.example.yveddr.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class Friend(
    val id: Int,
    val name: String,
    val birthday: LocalDate,
    val photoUrl: String? = null,
    val emoji: String = "🎂",
    val favoriteColor: String = "#FFE91E63",
    val isFavorite: Boolean = false,
    val giftIdea: String = "",
    val notes: String = "",
    val relationshipType: RelationshipType = RelationshipType.FRIEND
) {
    fun getDaysUntilBirthday(): Long {
        val today = LocalDate.now()
        val thisYearBirthday = birthday.withYear(today.year)
        val nextBirthday = if (thisYearBirthday.isBefore(today) || thisYearBirthday.isEqual(today)) {
            if (thisYearBirthday.isEqual(today)) return 0
            birthday.withYear(today.year + 1)
        } else {
            thisYearBirthday
        }
        return ChronoUnit.DAYS.between(today, nextBirthday)
    }
    
    fun getFormattedBirthday(): String {
        return birthday.format(DateTimeFormatter.ofPattern("dd MMM"))
    }
    
    fun getFullFormattedBirthday(): String {
        return birthday.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
    }
    
    fun getAge(): Int {
        return LocalDate.now().year - birthday.year
    }
    
    fun isBirthdayToday(): Boolean {
        val today = LocalDate.now()
        return birthday.dayOfMonth == today.dayOfMonth && birthday.month == today.month
    }
    
    fun isBirthdaySoon(): Boolean {
        return getDaysUntilBirthday() in 1..7
    }
    
    fun getUrgencyLevel(): UrgencyLevel {
        val days = getDaysUntilBirthday()
        return when {
            days == 0L -> UrgencyLevel.TODAY
            days <= 3 -> UrgencyLevel.VERY_URGENT
            days <= 7 -> UrgencyLevel.URGENT
            days <= 14 -> UrgencyLevel.SOON
            else -> UrgencyLevel.NORMAL
        }
    }
}

enum class RelationshipType(val displayName: String, val emoji: String) {
    FRIEND("Друг", "👥"),
    FAMILY("Семья", "👨‍👩‍👧‍👦"),
    COLLEAGUE("Коллега", "💼"),
    PARTNER("Партнёр", "💑"),
    OTHER("Другое", "✨")
}

enum class UrgencyLevel {
    TODAY, VERY_URGENT, URGENT, SOON, NORMAL
}