package com.example.yveddr.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yveddr.ui.theme.*

@Composable
fun StatisticsCard(
    totalFriends: Int,
    favoritesCount: Int,
    upcomingCount: Int,
    todayCount: Int,
    modifier: Modifier = Modifier
) {
    // Анимация пульсации
    val infiniteTransition = rememberInfiniteTransition(label = "stats_pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            WhitePink.copy(alpha = 0.4f),
                            LavenderPink.copy(alpha = 0.3f),
                            Color.White
                        )
                    )
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📊 Статистика",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = HelloKittyPink,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    emoji = "👥",
                    value = totalFriends.toString(),
                    label = "Всего",
                    color = HelloKittyPink
                )
                
                StatItem(
                    emoji = "⭐",
                    value = favoritesCount.toString(),
                    label = "Избранные",
                    color = StarYellow
                )
                
                StatItem(
                    emoji = "📅",
                    value = upcomingCount.toString(),
                    label = "Скоро",
                    color = LavenderPink,
                    scale = if (upcomingCount > 0) pulse else 1f
                )
                
                if (todayCount > 0) {
                    StatItem(
                        emoji = "🎉",
                        value = todayCount.toString(),
                        label = "Сегодня",
                        color = TodayBirthdayColor,
                        scale = pulse
                    )
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    emoji: String,
    value: String,
    label: String,
    color: Color,
    scale: Float = 1f,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.scale(scale)
    ) {
        Text(
            text = emoji,
            fontSize = 32.sp
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = color
        )
        
        Text(
            text = label,
            fontSize = 12.sp,
            color = color.copy(alpha = 0.7f)
        )
    }
}
