package com.example.yveddr.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yveddr.model.Friend
import com.example.yveddr.ui.components.*
import com.example.yveddr.ui.theme.*
import com.example.yveddr.viewmodel.BirthdayViewModel

@Composable
fun HomeScreen(
    viewModel: BirthdayViewModel = viewModel(),
    onEditFriend: (Friend) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFF5F8),  // Очень светлый розовый сверху
                            Color(0xFFFFF8FA),  // Почти белый
                            Color(0xFFFFFBFC),  // Белый с лёгким оттенком
                            Color.White         // Чисто белый внизу
                        )
                    )
                )
        )
        
        FloatingHearts(
            modifier = Modifier.fillMaxSize(),
            heartCount = 5
        )
        
        SparkleEffect(
            modifier = Modifier.fillMaxSize(),
            sparkleCount = 10
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                KawaiiHeader()
            }
            
            item {
                TestNotificationButton(viewModel = viewModel)
            }
            
            if (uiState.todaysBirthdays.isNotEmpty()) {
                item {
                    Text(
                        text = "🎉 Сегодня праздник! 🎉",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TodayBirthdayColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }
                
                items(
                    items = uiState.todaysBirthdays,
                    key = { it.id },
                    contentType = { "birthdayCake" }
                ) { friend ->
                    BirthdayCakeCard(
                        friend = friend,
                        onEdit = { onEditFriend(friend) },
                        onDelete = { viewModel.removeFriend(friend.id) }
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = WhitePink.copy(alpha = 0.9f)
                    ),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📅 Предстоящие дни рождения",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrightPink,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "✨ Не забудьте поздравить! ✨",
                            fontSize = 14.sp,
                            color = HelloKittyPink,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            if (uiState.upcomingBirthdays.isEmpty()) {
                item {
                    EmptyStateWithHelloKitty()
                }
            } else {
                items(
                    items = uiState.upcomingBirthdays,
                    key = { it.id },
                    contentType = { "friendCard" }
                ) { friend ->
                    FriendCard(
                        friend = friend,
                        onEdit = { onEditFriend(friend) },
                        onDelete = { viewModel.removeFriend(friend.id) },
                        onFavoriteToggle = { viewModel.toggleFavorite(friend.id) }
                    )
                }
            }
            
            item {
                HelloKittyCharacter(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                )
            }
            
            item {
                Text(
                    text = "Made with 💕 by Hello Kitty",
                    fontSize = 12.sp,
                    color = HelloKittyPink.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun KawaiiHeader() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = WhitePink.copy(alpha = 0.95f)
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            HelloKittyPink.copy(alpha = 0.2f),
                            RosePink.copy(alpha = 0.2f),
                            LavenderPink.copy(alpha = 0.2f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "🎀 Hello Kitty Birthday Reminder 🎀",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrightPink,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Никогда не забывай о днях рождения друзей! 💕",
                    fontSize = 13.sp,
                    color = HelloKittyPink.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun EmptyStateWithHelloKitty() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        HelloKittyCharacter()
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Пока нет друзей для отслеживания 💕",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = BrightPink,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Добавьте друзей, чтобы не забывать об их днях рождения!",
            fontSize = 14.sp,
            color = HelloKittyPink.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "✨🎂🎁🎈💝✨",
            fontSize = 32.sp,
            textAlign = TextAlign.Center
        )
    }
}