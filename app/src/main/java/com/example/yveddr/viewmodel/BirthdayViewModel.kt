package com.example.yveddr.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yveddr.model.Friend
import com.example.yveddr.notifications.NotificationScheduler
import com.example.yveddr.notifications.NotificationService
import com.example.yveddr.notifications.PermissionManager
import com.example.yveddr.repository.FriendRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BirthdayViewModel(private val context: Context) : ViewModel() {
    
    private val repository = FriendRepository(context)
    private val notificationService = NotificationService(context)
    private val notificationScheduler = NotificationScheduler(context)
    private val permissionManager = PermissionManager(context)
    
    private val _uiState = MutableStateFlow(BirthdayUiState())
    val uiState: StateFlow<BirthdayUiState> = _uiState.asStateFlow()
    
    init {
        loadFriends()
    }
    
    private fun loadFriends() {
        viewModelScope.launch {
            repository.friends.collect { friends ->
                _uiState.value = _uiState.value.copy(
                    allFriends = friends,
                    upcomingBirthdays = repository.getFriendsWithUpcomingBirthdays(),
                    todaysBirthdays = repository.getTodaysBirthdays()
                )
            }
        }
    }
    
    fun addFriend(friend: Friend) {
        repository.addFriend(friend)
        scheduleNotificationsForAllFriends()
    }
    
    fun removeFriend(friendId: Int) {
        repository.removeFriend(friendId)
        scheduleNotificationsForAllFriends()
    }
    
    fun updateFriend(friend: Friend) {
        repository.updateFriend(friend)
        scheduleNotificationsForAllFriends()
    }
    
    fun toggleFavorite(friendId: Int) {
        val friend = _uiState.value.allFriends.find { it.id == friendId }
        friend?.let {
            val updatedFriend = it.copy(isFavorite = !it.isFavorite)
            repository.updateFriend(updatedFriend)
        }
    }
    
    fun updateSelectedTab(tab: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }
    
    fun sendTestNotification() {
        notificationService?.sendTestNotification()
    }
    
    fun hasNotificationPermission(): Boolean {
        return permissionManager?.hasNotificationPermission() ?: false
    }
    
    private fun scheduleNotificationsForAllFriends() {
        viewModelScope.launch {
            val friends = repository.friends.value
            notificationScheduler?.scheduleBirthdayNotifications(friends)
        }
    }
    
    fun checkAndSendTodaysBirthdayNotifications() {
        viewModelScope.launch {
            val todaysBirthdays = repository.getTodaysBirthdays()
            todaysBirthdays.forEach { friend ->
                notificationService?.sendBirthdayNotification(friend)
            }
        }
    }
    
    fun getFriendById(friendId: Int): Friend? {
        return _uiState.value.allFriends.find { it.id == friendId }
    }
    
    fun getFavorites(): List<Friend> {
        return _uiState.value.allFriends.filter { it.isFavorite }
    }
    
    fun getFriendsCount(): Int {
        return _uiState.value.allFriends.size
    }
    
    fun getUpcomingCount(): Int {
        return _uiState.value.upcomingBirthdays.size
    }
}

data class BirthdayUiState(
    val allFriends: List<Friend> = emptyList(),
    val upcomingBirthdays: List<Friend> = emptyList(),
    val todaysBirthdays: List<Friend> = emptyList(),
    val selectedTab: Int = 0
)