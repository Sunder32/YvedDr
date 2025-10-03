package com.example.yveddr.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.example.yveddr.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Kawaii эффект сверкающих звёздочек ✨
 * @param sparkleCount количество звёздочек (уменьшите для производительности)
 */
@Composable
fun SparkleEffect(
    modifier: Modifier = Modifier,
    sparkleCount: Int = 20
) {
    val optimizedCount = sparkleCount.coerceIn(5, 30)
    
    val sparkles = remember {
        List(optimizedCount) {
            Sparkle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 8f + 4f,
                delay = Random.nextInt(2000)
            )
        }
    }
    
    val scales = sparkles.map { sparkle ->
        val infiniteTransition = rememberInfiniteTransition(label = "sparkle_${sparkle.hashCode()}")
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 2000 + sparkle.delay
                    0f at 0
                    1f at 500 + sparkle.delay
                    0f at 1000 + sparkle.delay
                },
                repeatMode = RepeatMode.Restart
            ),
            label = "sparkle_scale_${sparkle.hashCode()}"
        )
    }
    
    Canvas(modifier = modifier.fillMaxSize()) {
        sparkles.forEachIndexed { index, sparkle ->
            val scale = scales[index].value
            
            val x = size.width * sparkle.x
            val y = size.height * sparkle.y
            
            drawStar(
                center = Offset(x, y),
                size = sparkle.size * scale,
                color = SparkleWhite.copy(alpha = 0.8f * scale)
            )
        }
    }
}

private data class Sparkle(
    val x: Float,
    val y: Float,
    val size: Float,
    val delay: Int
)

private fun DrawScope.drawStar(
    center: Offset,
    size: Float,
    color: Color
) {
    val path = Path().apply {
        val radius = size
        val innerRadius = size * 0.4f
        
        for (i in 0 until 8) {
            val angle = (i * 45f - 90f) * Math.PI / 180f
            val r = if (i % 2 == 0) radius else innerRadius
            val x = center.x + (r * cos(angle)).toFloat()
            val y = center.y + (r * sin(angle)).toFloat()
            
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }
    
    drawPath(path, color)
}

/**
 * Плавающие сердечки 💕
 * @param heartCount количество сердечек (уменьшите для производительности)
 */
@Composable
fun FloatingHearts(
    modifier: Modifier = Modifier,
    heartCount: Int = 10
) {
    val optimizedCount = heartCount.coerceIn(3, 20)
    
    val hearts = remember {
        List(optimizedCount) {
            FloatingHeart(
                startX = Random.nextFloat(),
                startY = 1f + Random.nextFloat() * 0.2f,
                speed = Random.nextFloat() * 0.3f + 0.2f,
                size = Random.nextFloat() * 20f + 15f,
                delay = Random.nextInt(3000)
            )
        }
    }
    
    Canvas(modifier = modifier.fillMaxSize()) {
        hearts.forEach { heart ->
            val progress = ((System.currentTimeMillis() % (10000L + heart.delay)) / (10000f + heart.delay))
            
            val x = size.width * heart.startX
            val y = size.height * (heart.startY - progress * heart.speed * 1.5f)
            
            if (y > -heart.size) {
                drawHeart(
                    center = Offset(x, y),
                    size = heart.size,
                    color = HeartRed.copy(alpha = 0.3f * (1f - progress))
                )
            }
        }
    }
}

private data class FloatingHeart(
    val startX: Float,
    val startY: Float,
    val speed: Float,
    val size: Float,
    val delay: Int
)

private fun DrawScope.drawHeart(
    center: Offset,
    size: Float,
    color: Color
) {
    val path = Path().apply {
        val width = size
        val height = size
        
        moveTo(center.x, center.y + height * 0.3f)
        cubicTo(
            center.x - width * 0.5f, center.y - height * 0.1f,
            center.x - width * 0.5f, center.y - height * 0.5f,
            center.x, center.y - height * 0.3f
        )
        
        cubicTo(
            center.x + width * 0.5f, center.y - height * 0.5f,
            center.x + width * 0.5f, center.y - height * 0.1f,
            center.x, center.y + height * 0.3f
        )
        close()
    }
    
    drawPath(path, color)
}

/**
 * Kawaii блестки при нажатии
 */
@Composable
fun ConfettiEffect(
    trigger: Boolean,
    modifier: Modifier = Modifier,
    onComplete: () -> Unit = {}
) {
    var active by remember { mutableStateOf(false) }
    
    LaunchedEffect(trigger) {
        if (trigger) {
            active = true
            kotlinx.coroutines.delay(2000)
            active = false
            onComplete()
        }
    }
    
    if (active) {
        val confettiPieces = remember {
            List(50) {
                ConfettiPiece(
                    startX = 0.5f,
                    startY = 0.5f,
                    velocityX = (Random.nextFloat() - 0.5f) * 2f,
                    velocityY = Random.nextFloat() * -2f - 1f,
                    color = listOf(
                        HelloKittyPink,
                        BrightPink,
                        RosePink,
                        StarYellow,
                        LavenderPink
                    ).random(),
                    rotation = Random.nextFloat() * 360f
                )
            }
        }
        
        Canvas(modifier = modifier.fillMaxSize()) {
            val time = (System.currentTimeMillis() % 2000) / 2000f
            
            confettiPieces.forEach { piece ->
                val x = size.width * (piece.startX + piece.velocityX * time)
                val y = size.height * (piece.startY + piece.velocityY * time + time * time * 2f)
                
                if (y < size.height) {
                    rotate(piece.rotation * time * 360f, pivot = Offset(x, y)) {
                        drawRect(
                            color = piece.color.copy(alpha = 1f - time),
                            topLeft = Offset(x - 8f, y - 8f),
                            size = androidx.compose.ui.geometry.Size(16f, 16f)
                        )
                    }
                }
            }
        }
    }
}

private data class ConfettiPiece(
    val startX: Float,
    val startY: Float,
    val velocityX: Float,
    val velocityY: Float,
    val color: Color,
    val rotation: Float
)

/**
 * Мерцающий бордюр для карточек
 */
@Composable
fun ShimmerBorder(
    modifier: Modifier = Modifier,
    color: Color = HelloKittyPink
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )
    
    Canvas(modifier = modifier) {
        val gradient = androidx.compose.ui.graphics.Brush.sweepGradient(
            0f to color.copy(alpha = 0.3f),
            offset to color.copy(alpha = 1f),
            (offset + 0.1f).coerceAtMost(1f) to color.copy(alpha = 0.3f),
            1f to color.copy(alpha = 0.3f),
            center = Offset(size.width / 2, size.height / 2)
        )
        
        drawCircle(brush = gradient)
    }
}
