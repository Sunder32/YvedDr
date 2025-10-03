package com.example.yveddr.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yveddr.model.Friend
import com.example.yveddr.model.RelationshipType
import com.example.yveddr.ui.components.AnimatedFab
import com.example.yveddr.ui.components.FriendCard
import com.example.yveddr.ui.components.FloatingHearts
import com.example.yveddr.ui.components.SparkleEffect
import com.example.yveddr.ui.theme.*
import com.example.yveddr.viewmodel.BirthdayViewModel

enum class FriendFilter(val label: String, val icon: String) {
    ALL("Все", "🌟"),
    FAVORITES("Избранные", "⭐"),
    FAMILY("Семья", "👨‍👩‍👧‍👦"),
    FRIENDS("Друзья", "👥"),
    COLLEAGUES("Коллеги", "💼")
}

enum class SortType(val label: String) {
    BY_URGENCY("По срочности"),
    BY_BIRTHDAY("По дате ДР"),
    BY_NAME("По имени")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsListScreen(
    viewModel: BirthdayViewModel = viewModel(),
    onAddFriendClick: () -> Unit = {},
    onEditFriend: (Friend) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedFilter by remember { mutableStateOf(FriendFilter.ALL) }
    var selectedSort by remember { mutableStateOf(SortType.BY_URGENCY) }
    var searchQuery by remember { mutableStateOf("") }
    var showSearch by remember { mutableStateOf(false) }
    var showStats by remember { mutableStateOf(false) }
    
    val filteredFriends = remember(uiState.allFriends, selectedFilter, searchQuery) {
        var friends = when (selectedFilter) {
            FriendFilter.ALL -> uiState.allFriends
            FriendFilter.FAVORITES -> uiState.allFriends.filter { it.isFavorite }
            FriendFilter.FAMILY -> uiState.allFriends.filter { it.relationshipType == RelationshipType.FAMILY }
            FriendFilter.FRIENDS -> uiState.allFriends.filter { it.relationshipType == RelationshipType.FRIEND }
            FriendFilter.COLLEAGUES -> uiState.allFriends.filter { it.relationshipType == RelationshipType.COLLEAGUE }
        }
        
        if (searchQuery.isNotEmpty()) {
            friends = friends.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
        
        friends
    }
    
    val sortedFriends = remember(filteredFriends, selectedSort) {
        when (selectedSort) {
            SortType.BY_BIRTHDAY -> filteredFriends.sortedWith(
                compareByDescending<Friend> { it.isFavorite }
                    .thenBy { it.getDaysUntilBirthday() }
            )
            SortType.BY_NAME -> filteredFriends.sortedWith(
                compareByDescending<Friend> { it.isFavorite }
                    .thenBy { it.name }
            )
            SortType.BY_URGENCY -> filteredFriends.sortedWith(
                compareByDescending<Friend> { it.isFavorite }
                    .thenBy { it.getUrgencyLevel().ordinal }
                    .thenBy { it.getDaysUntilBirthday() }
            )
        }
    }

    Scaffold(
        topBar = {
            Surface(
                shadowElevation = 4.dp,
                color = Color.White
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "👥 Друзья",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = HelloKittyPink
                            )
                            Text(
                                text = "${sortedFriends.size} ${getFriendsCountText(sortedFriends.size)}",
                                fontSize = 14.sp,
                                color = HelloKittyPink.copy(alpha = 0.6f)
                            )
                        }
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(
                                onClick = { showSearch = !showSearch },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (showSearch) HelloKittyPink else LavenderPink)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Поиск",
                                    tint = if (showSearch) Color.White else HelloKittyPink
                                )
                            }
                            
                            IconButton(
                                onClick = { showStats = !showStats },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (showStats) HelloKittyPink else LavenderPink)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Статистика",
                                    tint = if (showStats) Color.White else HelloKittyPink
                                )
                            }
                            
                            IconButton(
                                onClick = {
                                    selectedSort = when (selectedSort) {
                                        SortType.BY_URGENCY -> SortType.BY_BIRTHDAY
                                        SortType.BY_BIRTHDAY -> SortType.BY_NAME
                                        SortType.BY_NAME -> SortType.BY_URGENCY
                                    }
                                },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(LavenderPink)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Sort,
                                    contentDescription = selectedSort.label,
                                    tint = HelloKittyPink
                                )
                            }
                        }
                    }
                    
                    AnimatedVisibility(
                        visible = showSearch,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut() + slideOutVertically()
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            placeholder = { Text("Поиск по имени...") },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null)
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Очистить")
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HelloKittyPink,
                                unfocusedBorderColor = LavenderPink
                            )
                        )
                    }
                    
                    AnimatedVisibility(
                        visible = showStats,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut() + slideOutVertically()
                    ) {
                        QuickStatsRow(
                            totalFriends = uiState.allFriends.size,
                            favoritesCount = uiState.allFriends.count { it.isFavorite },
                            upcomingCount = uiState.upcomingBirthdays.size,
                            todayCount = uiState.todaysBirthdays.size
                        )
                    }
                    
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(FriendFilter.values()) { filter ->
                            FilterChip(
                                selected = selectedFilter == filter,
                                onClick = { selectedFilter = filter },
                                label = { Text("${filter.icon} ${filter.label}") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = HelloKittyPink,
                                    selectedLabelColor = Color.White,
                                    containerColor = LavenderPink,
                                    labelColor = HelloKittyPink
                                )
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            AnimatedFab(
                onClick = onAddFriendClick
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFFF5F8),
                                Color(0xFFFFFBFC),
                                Color.White
                            )
                        )
                    )
            )
            
            FloatingHearts(modifier = Modifier.fillMaxSize(), heartCount = 3)
            SparkleEffect(modifier = Modifier.fillMaxSize(), sparkleCount = 6)
            
            if (sortedFriends.isEmpty()) {
                EmptyStateView(filter = selectedFilter)
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(
                        items = sortedFriends,
                        key = { it.id },
                        contentType = { "friendCard" }
                    ) { friend ->
                        FriendCard(
                            friend = friend,
                            onClick = { },
                            onEdit = { onEditFriend(friend) },
                            onDelete = { viewModel.removeFriend(friend.id) },
                            onFavoriteToggle = { viewModel.toggleFavorite(friend.id) }
                        )
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickStatsRow(
    totalFriends: Int,
    favoritesCount: Int,
    upcomingCount: Int,
    todayCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(LavenderPink, WhitePink)
                )
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(icon = "👥", value = totalFriends.toString(), label = "Всего")
        StatItem(icon = "⭐", value = favoritesCount.toString(), label = "Избр.")
        StatItem(icon = "📅", value = upcomingCount.toString(), label = "Скоро")
        StatItem(icon = "🎉", value = todayCount.toString(), label = "Сегодня")
    }
}

@Composable
private fun StatItem(icon: String, value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 20.sp
        )
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = HelloKittyPink
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = HelloKittyPink.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun EmptyStateView(filter: FriendFilter) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = when (filter) {
                    FriendFilter.ALL -> "🌸"
                    FriendFilter.FAVORITES -> "⭐"
                    FriendFilter.FAMILY -> "👨‍👩‍👧‍👦"
                    FriendFilter.FRIENDS -> "👥"
                    FriendFilter.COLLEAGUES -> "💼"
                },
                fontSize = 64.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = when (filter) {
                    FriendFilter.ALL -> "Пока нет друзей"
                    FriendFilter.FAVORITES -> "Нет избранных друзей"
                    FriendFilter.FAMILY -> "Нет членов семьи"
                    FriendFilter.FRIENDS -> "Нет друзей в этой категории"
                    FriendFilter.COLLEAGUES -> "Нет коллег"
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = HelloKittyPink,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = when (filter) {
                    FriendFilter.ALL -> "Добавьте первого друга! 💕"
                    else -> "Попробуйте другой фильтр"
                },
                fontSize = 16.sp,
                color = HelloKittyPink.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun getFriendsCountText(count: Int): String {
    return when {
        count % 10 == 1 && count % 100 != 11 -> "друг"
        count % 10 in 2..4 && count % 100 !in 12..14 -> "друга"
        else -> "друзей"
    }
}
