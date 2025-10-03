package com.example.yveddr.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.yveddr.model.Friend
import com.example.yveddr.ui.components.AppHeader
import com.example.yveddr.ui.screens.AddFriendScreen
import com.example.yveddr.ui.screens.FriendsListScreen
import com.example.yveddr.ui.screens.HomeScreen
import com.example.yveddr.ui.theme.HelloKittyPink
import com.example.yveddr.viewmodel.BirthdayViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Главная", Icons.Default.Home)
    object Friends : Screen("friends", "Друзья", Icons.Default.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation() {
    val context = LocalContext.current
    val viewModel = remember { BirthdayViewModel(context) }
    val uiState by viewModel.uiState.collectAsState()
    var showAddFriend by remember { mutableStateOf(false) }
    var editingFriend by remember { mutableStateOf<Friend?>(null) }
    
    editingFriend?.let { friend ->
        AddFriendScreen(
            viewModel = viewModel,
            onNavigateBack = { editingFriend = null },
            editingFriend = friend
        )
        return
    }
    
    if (showAddFriend) {
        AddFriendScreen(
            viewModel = viewModel,
            onNavigateBack = { showAddFriend = false }
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFF0F5),  // Розовый сверху
                            Color(0xFFFFF5F8),
                            Color(0xFFFFFAFC),
                            Color(0xFFFFFBFC),
                            Color.White
                        )
                    )
                )
        ) {
            Scaffold(
                topBar = {
                    AppHeader()
                },
                bottomBar = {
                    NavigationBar(
                        containerColor = Color(0x00FFFFFF), // Полностью прозрачный
                        tonalElevation = 0.dp,
                        modifier = Modifier
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0x00FFFFFF),
                                        Color(0x30FFE4EC),
                                        Color(0x50FFD6E8)
                                    )
                                )
                            )
                    ) {
                        val screens = listOf(Screen.Home, Screen.Friends)
                        
                        screens.forEachIndexed { index, screen ->
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        imageVector = screen.icon,
                                        contentDescription = screen.title
                                    )
                                },
                                label = { Text(screen.title) },
                                selected = uiState.selectedTab == index,
                                onClick = { viewModel.updateSelectedTab(index) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = HelloKittyPink,
                                    selectedTextColor = HelloKittyPink,
                                    indicatorColor = Color(0x30FFB6D9),
                                    unselectedIconColor = HelloKittyPink.copy(alpha = 0.5f),
                                    unselectedTextColor = HelloKittyPink.copy(alpha = 0.5f)
                                )
                            )
                        }
                    }
                },
                containerColor = Color.Transparent,
                contentWindowInsets = WindowInsets(0, 0, 0, 0) // Убираем стандартные отступы
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    when (uiState.selectedTab) {
                        0 -> HomeScreen(
                            viewModel = viewModel,
                            onEditFriend = { friend: Friend -> editingFriend = friend }
                        )
                        1 -> FriendsListScreen(
                            viewModel = viewModel,
                            onAddFriendClick = { showAddFriend = true },
                            onEditFriend = { friend: Friend -> editingFriend = friend }
                        )
                    }
                }
            }
        }
    }
}