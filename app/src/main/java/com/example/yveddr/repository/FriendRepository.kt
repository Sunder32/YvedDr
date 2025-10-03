package com.example.yveddr.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.yveddr.model.Friend
import com.example.yveddr.utils.LocalDateAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class FriendRepository(context: Context) {
    
    private val TAG = "FriendRepository"
    private val prefs: SharedPreferences = context.getSharedPreferences("friends_prefs", Context.MODE_PRIVATE)
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()
    private val _friends = MutableStateFlow(loadFriends())
    val friends: StateFlow<List<Friend>> = _friends.asStateFlow()
    
    private fun loadFriends(): List<Friend> {
        val json = prefs.getString("friends_list", null)
        return if (json != null) {
            try {
                val type = object : TypeToken<List<Friend>>() {}.type
                gson.fromJson(json, type)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading friends", e)
                getSampleFriends()
            }
        } else {
            getSampleFriends()
        }
    }
    
    private fun saveFriends() {
        try {
            val json = gson.toJson(_friends.value)
            prefs.edit().putString("friends_list", json).apply()
            Log.d(TAG, "Friends saved successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving friends", e)
        }
    }
    
    private fun generateUniqueId(): Int {
        val existingIds = _friends.value.map { it.id }.toSet()
        var newId: Int
        do {
            newId = (System.nanoTime() % Int.MAX_VALUE).toInt().let { 
                if (it < 0) -it else it 
            }
        } while (existingIds.contains(newId))
        return newId
    }
    
    fun addFriend(friend: Friend) {
        val currentFriends = _friends.value.toMutableList()
        val friendToAdd = if (_friends.value.any { it.id == friend.id }) {
            val newId = generateUniqueId()
            Log.w(TAG, "Duplicate ID detected: ${friend.id}, generating new: $newId")
            friend.copy(id = newId)
        } else {
            Log.d(TAG, "Adding friend with ID: ${friend.id}")
            friend
        }
        currentFriends.add(friendToAdd)
        _friends.value = currentFriends
        saveFriends()
        
        val ids = _friends.value.map { it.id }
        val duplicates = ids.groupingBy { it }.eachCount().filter { it.value > 1 }
        if (duplicates.isNotEmpty()) {
            Log.e(TAG, "CRITICAL: Duplicate IDs found after add: $duplicates")
        }
    }
    
    fun removeFriend(friendId: Int) {
        val currentFriends = _friends.value.toMutableList()
        currentFriends.removeIf { it.id == friendId }
        _friends.value = currentFriends
        saveFriends()
    }
    
    fun updateFriend(friend: Friend) {
        val currentFriends = _friends.value.toMutableList()
        val index = currentFriends.indexOfFirst { it.id == friend.id }
        if (index != -1) {
            currentFriends[index] = friend
            _friends.value = currentFriends
            saveFriends()
        }
    }
    
    fun getFriendsWithUpcomingBirthdays(): List<Friend> {
        return _friends.value
            .sortedBy { it.getDaysUntilBirthday() }
            .take(5)
    }
    
    fun getTodaysBirthdays(): List<Friend> {
        return _friends.value.filter { it.isBirthdayToday() }
    }
    
    private fun getSampleFriends(): List<Friend> {
        return listOf(
            Friend(
                1, 
                "Анна", 
                LocalDate.of(1995, 10, 15),
                emoji = "🌸",
                favoriteColor = "#FFE91E63",
                isFavorite = true,
                giftIdea = "🎀 Набор Hello Kitty",
                relationshipType = com.example.yveddr.model.RelationshipType.FRIEND
            ),
            Friend(
                2, 
                "Мария", 
                LocalDate.of(1998, 10, 2),
                emoji = "🎀",
                favoriteColor = "#FFEC407A",
                isFavorite = true,
                giftIdea = "💖 Розовая плюшевая игрушка",
                notes = "Обожает милые вещи!",
                relationshipType = com.example.yveddr.model.RelationshipType.FRIEND
            ),
            Friend(
                3, 
                "Екатерина", 
                LocalDate.of(1997, 11, 8),
                emoji = "🦋",
                favoriteColor = "#FFF8BBD9",
                giftIdea = "🎉 Канцелярия kawaii",
                relationshipType = com.example.yveddr.model.RelationshipType.COLLEAGUE
            ),
            Friend(
                4, 
                "София", 
                LocalDate.of(1996, 12, 20),
                emoji = "🌟",
                favoriteColor = "#FFFCE4EC",
                isFavorite = false,
                giftIdea = "✨ Украшения",
                relationshipType = com.example.yveddr.model.RelationshipType.FRIEND
            ),
            Friend(
                5, 
                "Дарья", 
                LocalDate.of(1999, 1, 14),
                emoji = "🌷",
                favoriteColor = "#FFFFC0CB",
                giftIdea = "💝 Косметика",
                relationshipType = com.example.yveddr.model.RelationshipType.FAMILY
            ),
            Friend(
                6, 
                "Алиса", 
                LocalDate.of(1994, 3, 25),
                emoji = "🎈",
                favoriteColor = "#FFFF69B4",
                isFavorite = true,
                giftIdea = "🎨 Товары для творчества",
                notes = "Любит рисовать",
                relationshipType = com.example.yveddr.model.RelationshipType.FRIEND
            ),
            Friend(
                7,
                "Лиза",
                LocalDate.of(1997, 10, 5),
                emoji = "🍒",
                favoriteColor = "#FFFF1493",
                giftIdea = "🍭 Сладости",
                relationshipType = com.example.yveddr.model.RelationshipType.FRIEND
            ),
            Friend(
                8,
                "Наталья",
                LocalDate.of(1996, 10, 10),
                emoji = "💞",
                favoriteColor = "#FFDB7093",
                isFavorite = true,
                giftIdea = "📚 Книги",
                notes = "Любит читать",
                relationshipType = com.example.yveddr.model.RelationshipType.FAMILY
            )
        )
    }
}