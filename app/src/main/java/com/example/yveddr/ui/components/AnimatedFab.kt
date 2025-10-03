package com.example.yveddr.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.yveddr.ui.theme.BrightPink
import com.example.yveddr.ui.theme.HelloKittyPink

@Composable
fun AnimatedFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Анимация пульсации
    val infiniteTransition = rememberInfiniteTransition(label = "fab_pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_animation"
    )
    
    var clicked by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (clicked) 45f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "rotation_animation"
    )
    
    FloatingActionButton(
        onClick = {
            clicked = !clicked
            onClick()
        },
        modifier = modifier.scale(pulse),
        containerColor = BrightPink,
        contentColor = Color.White,
        shape = CircleShape,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 12.dp,
            pressedElevation = 16.dp
        )
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Friend",
            modifier = Modifier
                .size(28.dp)
                .rotate(rotation)
        )
    }
}