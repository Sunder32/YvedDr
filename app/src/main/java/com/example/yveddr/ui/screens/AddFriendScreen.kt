package com.example.yveddr.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Note
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yveddr.model.Friend
import com.example.yveddr.model.RelationshipType
import com.example.yveddr.ui.theme.*
import com.example.yveddr.viewmodel.BirthdayViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendScreen(
    viewModel: BirthdayViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    editingFriend: Friend? = null
) {
    val isEditing = editingFriend != null
    
    var name by remember { mutableStateOf(editingFriend?.name ?: "") }
    var selectedDate by remember { mutableStateOf<LocalDate?>(editingFriend?.birthday) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedEmoji by remember { mutableStateOf(editingFriend?.emoji ?: "🎂") }
    var selectedRelationship by remember { mutableStateOf(editingFriend?.relationshipType ?: RelationshipType.FRIEND) }
    var giftIdea by remember { mutableStateOf(editingFriend?.giftIdea ?: "") }
    var notes by remember { mutableStateOf(editingFriend?.notes ?: "") }
    var isFavorite by remember { mutableStateOf(editingFriend?.isFavorite ?: false) }
    var showEmojiPicker by remember { mutableStateOf(false) }
    
    val datePickerState = rememberDatePickerState()
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    
    val emojiList = listOf(
        "🎂", "🎈", "🎀", "🎁", "🎉", "🎊", "🌸", "🌺", "🌷",
        "🦋", "🌟", "⭐", "💐", "🌹", "💝", "💖", "💕", "💗",
        "🍰", "🧁", "🍓", "🍒", "🍑", "🍊", "🍋", "🍉"
    )
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFF5F8),
                            Color(0xFFFFF8FA),
                            Color(0xFFFFFBFC),
                            Color.White
                        )
                    )
                )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF5F8)  // Очень светлый розовый
            ),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isEditing) "✨ Редактировать друга ✨" else "✨ Добавить нового друга ✨",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = HelloKittyPink,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isEditing) "🎀 Измени информацию о друге 🎀" else "🎀 Заполни информацию о друге 🎀",
                    fontSize = 14.sp,
                    color = HelloKittyPink.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White  // Чисто белый фон
            ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Выбери эмодзи",
                    fontSize = 14.sp,
                    color = HelloKittyPink,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                Button(
                    onClick = { showEmojiPicker = !showEmojiPicker },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HelloKittyPink
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = selectedEmoji,
                        fontSize = 36.sp
                    )
                }
                
                if (showEmojiPicker) {
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Column {
                        emojiList.chunked(6).forEach { row ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                row.forEach { emoji ->
                                    TextButton(
                                        onClick = {
                                            selectedEmoji = emoji
                                            showEmojiPicker = false
                                        },
                                        modifier = Modifier.size(48.dp)
                                    ) {
                                        Text(text = emoji, fontSize = 28.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Имя друга 💕", color = HelloKittyPink) },
            placeholder = { Text("Введите имя", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Name",
                    tint = HelloKittyPink
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = HelloKittyPink,
                focusedLabelColor = HelloKittyPink,
                unfocusedLabelColor = HelloKittyPink.copy(alpha = 0.7f),
                cursorColor = HelloKittyPink,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedBorderColor = HelloKittyPink.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.outlinedCardColors(
                containerColor = Color.Transparent
            ),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(HelloKittyPink)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Calendar",
                    tint = HelloKittyPink,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "День рождения 🎂",
                        fontSize = 12.sp,
                        color = HelloKittyPink,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = selectedDate?.format(formatter) ?: "Выберите дату",
                        fontSize = 16.sp,
                        color = if (selectedDate != null) Color.Black else Color.DarkGray,
                        fontWeight = if (selectedDate != null) FontWeight.Medium else FontWeight.Normal
                    )
                }
            }
        }
        
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                selectedDate = java.time.Instant
                                    .ofEpochMilli(millis)
                                    .atZone(java.time.ZoneId.systemDefault())
                                    .toLocalDate()
                            }
                            showDatePicker = false
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = HelloKittyPink
                        )
                    ) {
                        Text("ОК")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDatePicker = false },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = HelloKittyPink
                        )
                    ) {
                        Text("Отмена")
                    }
                },
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White
                )
            ) {
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors(
                        containerColor = Color.White,
                        selectedDayContainerColor = HelloKittyPink,
                        todayContentColor = HelloKittyPink,
                        todayDateBorderColor = HelloKittyPink
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Тип отношений",
                    fontSize = 14.sp,
                    color = HelloKittyPink,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                RelationshipType.values().forEach { type ->
                    FilterChip(
                        selected = selectedRelationship == type,
                        onClick = { selectedRelationship = type },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = type.emoji, fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = type.displayName)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = HelloKittyPink,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = giftIdea,
            onValueChange = { giftIdea = it },
            label = { Text("Идея подарка 🎁", color = HelloKittyPink) },
            placeholder = { Text("Что подарить?", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.CardGiftcard,
                    contentDescription = "Gift",
                    tint = HelloKittyPink
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = HelloKittyPink,
                focusedLabelColor = HelloKittyPink,
                unfocusedLabelColor = HelloKittyPink.copy(alpha = 0.7f),
                cursorColor = HelloKittyPink,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedBorderColor = HelloKittyPink.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Заметки 📝", color = HelloKittyPink) },
            placeholder = { Text("Дополнительная информация", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Note,
                    contentDescription = "Notes",
                    tint = HelloKittyPink
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = HelloKittyPink,
                focusedLabelColor = HelloKittyPink,
                unfocusedLabelColor = HelloKittyPink.copy(alpha = 0.7f),
                cursorColor = HelloKittyPink,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedBorderColor = HelloKittyPink.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(16.dp),
            maxLines = 4
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            onClick = { isFavorite = !isFavorite },
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isFavorite) HelloKittyPink.copy(alpha = 0.2f) 
                else Color.White
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isFavorite) "⭐" else "☆",
                        fontSize = 28.sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Добавить в избранное",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = HelloKittyPink
                        )
                        Text(
                            text = "Важные друзья будут выше",
                            fontSize = 12.sp,
                            color = HelloKittyPink.copy(alpha = 0.6f)
                        )
                    }
                }
                Switch(
                    checked = isFavorite,
                    onCheckedChange = { isFavorite = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = HelloKittyPink,
                        checkedTrackColor = HelloKittyPink.copy(alpha = 0.5f)
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = {
                if (name.isNotBlank() && selectedDate != null) {
                    if (isEditing && editingFriend != null) {
                        val updatedFriend = editingFriend.copy(
                            name = name.trim(),
                            birthday = selectedDate!!,
                            emoji = selectedEmoji,
                            isFavorite = isFavorite,
                            giftIdea = giftIdea.trim(),
                            notes = notes.trim(),
                            relationshipType = selectedRelationship
                        )
                        viewModel.updateFriend(updatedFriend)
                    } else {
                        val newFriend = Friend(
                            id = (System.nanoTime() % Int.MAX_VALUE).toInt().let { if (it < 0) -it else it },
                            name = name.trim(),
                            birthday = selectedDate!!,
                            emoji = selectedEmoji,
                            isFavorite = isFavorite,
                            giftIdea = giftIdea.trim(),
                            notes = notes.trim(),
                            relationshipType = selectedRelationship
                        )
                        viewModel.addFriend(newFriend)
                    }
                    onNavigateBack()
                }
            },
            enabled = name.isNotBlank() && selectedDate != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = HelloKittyPink,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = if (isEditing) "💾 Сохранить изменения 💕" else "✨ Добавить друга 💕",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = HelloKittyPink
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Отмена",
                fontSize = 16.sp
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        }
    }
}