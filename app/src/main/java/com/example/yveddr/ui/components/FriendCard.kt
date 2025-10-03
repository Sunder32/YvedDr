package com.example.yveddr.ui.components

import androidx.compose.animation.core.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yveddr.model.Friend
import com.example.yveddr.model.UrgencyLevel
import com.example.yveddr.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendCard(
    friend: Friend,
    onClick: () -> Unit = {},
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    onFavoriteToggle: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val daysUntil = friend.getDaysUntilBirthday()
    var expanded by remember(friend.id) { mutableStateOf(false) }
    var showDeleteDialog by remember(friend.id) { mutableStateOf(false) }
    
    val infiniteTransition = rememberInfiniteTransition(label = "urgent_pulse")
    val urgentPulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (friend.getUrgencyLevel() == UrgencyLevel.TODAY || 
                         friend.getUrgencyLevel() == UrgencyLevel.VERY_URGENT) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "urgent_pulse"
    )
    
    val cardColor = when (friend.getUrgencyLevel()) {
        UrgencyLevel.TODAY -> TodayBirthdayColor
        UrgencyLevel.VERY_URGENT -> UrgentColor
        UrgencyLevel.URGENT -> SoonColor
        UrgencyLevel.SOON -> LavenderPink
        UrgencyLevel.NORMAL -> CardBackground
    }
    
    Card(
        onClick = { expanded = !expanded },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .scale(urgentPulse)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = HelloKittyPink,
                spotColor = BrightPink
            ),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            GlassMorphismPink.copy(alpha = 0.6f),
                            VeryLightPink.copy(alpha = 0.8f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        HelloKittyPink,
                                        BrightPink
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = friend.emoji,
                            fontSize = 36.sp
                        )
                        
                        if (friend.isFavorite) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Favorite",
                                tint = StarYellow,
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.TopEnd)
                                    .offset(x = 4.dp, y = (-4).dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = friend.name,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrightPink,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = friend.relationshipType.emoji,
                                fontSize = 14.sp
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = when {
                                daysUntil == 0L -> "🎉 Сегодня день рождения!"
                                daysUntil == 1L -> "⏰ Завтра день рождения!"
                                daysUntil <= 7 -> "📅 Через $daysUntil ${getDaysText(daysUntil)}"
                                else -> "📆 $daysUntil ${getDaysText(daysUntil)} до ДР"
                            },
                            fontSize = 14.sp,
                            fontWeight = if (daysUntil <= 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (daysUntil <= 1) TodayBirthdayColor else HelloKittyPink.copy(alpha = 0.8f)
                        )
                        
                        Text(
                            text = "Будет ${friend.getAge() + 1} ${getYearsText(friend.getAge() + 1)} ✨",
                            fontSize = 12.sp,
                            color = HelloKittyPink.copy(alpha = 0.6f)
                        )
                    }
                    
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(
                            onClick = onFavoriteToggle,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (friend.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Toggle Favorite",
                                tint = if (friend.isFavorite) HeartRed else HelloKittyPink.copy(alpha = 0.5f),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        
                        IconButton(
                            onClick = onEdit,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Friend",
                                tint = HelloKittyPink,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
                
                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(WhitePink.copy(alpha = 0.5f))
                            .padding(16.dp)
                    ) {
                        Divider(
                            color = HelloKittyPink.copy(alpha = 0.3f),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        InfoRow(
                            icon = "🎂",
                            label = "День рождения",
                            value = friend.getFullFormattedBirthday()
                        )
                        
                        InfoRow(
                            icon = friend.relationshipType.emoji,
                            label = "Отношение",
                            value = friend.relationshipType.displayName
                        )
                        
                        if (friend.giftIdea.isNotEmpty()) {
                            InfoRow(
                                icon = "🎁",
                                label = "Идея подарка",
                                value = friend.giftIdea
                            )
                        }
                        
                        if (friend.notes.isNotEmpty()) {
                            InfoRow(
                                icon = "📝",
                                label = "Заметки",
                                value = friend.notes
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        OutlinedButton(
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = TodayBirthdayColor
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Удалить друга")
                        }
                    }
                }
            }
        }
    }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "💔 Удалить друга?",
                    color = HelloKittyPink,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Вы уверены, что хотите удалить ${friend.name} из списка? Это действие нельзя отменить.",
                    color = HelloKittyPink.copy(alpha = 0.8f)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    }
                ) {
                    Text(
                        text = "Удалить",
                        color = TodayBirthdayColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text(
                        text = "Отмена",
                        color = HelloKittyPink
                    )
                }
            },
            containerColor = WhitePink,
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
private fun InfoRow(
    icon: String,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = icon,
            fontSize = 18.sp,
            modifier = Modifier.width(32.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = HelloKittyPink.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = BrightPink,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

private fun getDaysText(days: Long): String {
    return when {
        days % 10L == 1L && days % 100L != 11L -> "день"
        days % 10L in 2L..4L && days % 100L !in 12L..14L -> "дня"
        else -> "дней"
    }
}

private fun getYearsText(years: Int): String {
    return when {
        years % 10 == 1 && years % 100 != 11 -> "год"
        years % 10 in 2..4 && years % 100 !in 12..14 -> "года"
        else -> "лет"
    }
}