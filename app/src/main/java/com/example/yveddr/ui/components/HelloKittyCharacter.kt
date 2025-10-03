package com.example.yveddr.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yveddr.ui.theme.*
import kotlin.math.sin
import kotlin.math.cos

@Composable
fun HelloKittyCharacter(
    modifier: Modifier = Modifier,
    size: Int = 140
) {
    // Анимация покачивания
    val infiniteTransition = rememberInfiniteTransition(label = "hello_kitty_sway")
    val sway by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sway_animation"
    )
    
    val blink by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 4000
                1f at 0
                1f at 3700
                0.1f at 3800
                1f at 3900
                1f at 4000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "blink_animation"
    )
    
    // Анимация блеска на бантике
    val bowShine by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "bow_shine"
    )
    
    // Вращение звёздочек
    val starRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "star_rotation"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            // Звёздочки вокруг Hello Kitty
            Canvas(
                modifier = Modifier.size((size + 60).dp)
            ) {
                drawStarsAround(starRotation, size.dp.toPx())
            }
            
            Canvas(
                modifier = Modifier
                    .size(size.dp)
                    .offset(x = sway.dp)
            ) {
                drawEnhancedHelloKitty(blink, bowShine)
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Милый текст с анимацией
        val textScale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500),
                repeatMode = RepeatMode.Reverse
            ),
            label = "text_scale"
        )
        
        Box(
            modifier = Modifier.graphicsLayer(scaleX = textScale, scaleY = textScale)
        ) {
            Text(
                text = "🎀 Kawaii! 🎀",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = HelloKittyPink,
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "✨ Hello Kitty ✨",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = BrightPink.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
    }
}

private fun DrawScope.drawStarsAround(rotation: Float, radius: Float) {
    val starCount = 8
    val starDistance = radius * 0.9f
    val centerX = size.width / 2
    val centerY = size.height / 2
    
    for (i in 0 until starCount) {
        val angle = (i * 360f / starCount + rotation) * Math.PI / 180
        val x = centerX + starDistance * cos(angle).toFloat()
        val y = centerY + starDistance * sin(angle).toFloat()
        
        drawStar(
            center = Offset(x, y),
            size = 8f + (i % 2) * 4f,
            color = if (i % 2 == 0) StarYellow else LavenderPink,
            rotation = rotation + i * 45f
        )
    }
}

private fun DrawScope.drawStar(
    center: Offset,
    size: Float,
    color: Color,
    rotation: Float = 0f
) {
    val path = Path().apply {
        val radius = size
        val innerRadius = size * 0.4f
        
        for (i in 0 until 8) {
            val angle = (i * 45f - 90f + rotation) * Math.PI / 180f
            val r = if (i % 2 == 0) radius else innerRadius
            val x = center.x + (r * cos(angle)).toFloat()
            val y = center.y + (r * sin(angle)).toFloat()
            
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }
    
    drawPath(path, color.copy(alpha = 0.7f))
}

private fun DrawScope.drawEnhancedHelloKitty(eyeScale: Float, bowShine: Float) {
    val centerX = size.width / 2f
    val centerY = size.height / 2f
    val radius = size.minDimension / 2.5f
    
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                SoftPink.copy(alpha = 0.3f),
                Color.Transparent
            ),
            center = Offset(centerX, centerY),
            radius = radius * 1.5f
        ),
        radius = radius * 1.5f,
        center = Offset(centerX, centerY)
    )
    
    // Тень под головой
    drawOval(
        color = Color.Black.copy(alpha = 0.1f),
        topLeft = Offset(centerX - radius * 0.8f, centerY + radius * 1.2f),
        size = Size(radius * 1.6f, radius * 0.3f)
    )
    
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color.White,
                VeryLightPink.copy(alpha = 0.3f)
            ),
            center = Offset(centerX, centerY)
        ),
        radius = radius,
        center = Offset(centerX, centerY)
    )
    
    drawCircle(
        color = Color.Black,
        radius = radius,
        center = Offset(centerX, centerY),
        style = Stroke(width = 3.5.dp.toPx())
    )
    
    // Румянец (щёчки)
    val blushColor = RosePink.copy(alpha = 0.4f)
    drawOval(
        color = blushColor,
        topLeft = Offset(centerX - radius * 0.8f, centerY + radius * 0.1f),
        size = Size(radius * 0.35f, radius * 0.25f)
    )
    drawOval(
        color = blushColor,
        topLeft = Offset(centerX + radius * 0.45f, centerY + radius * 0.1f),
        size = Size(radius * 0.35f, radius * 0.25f)
    )
    
    // Ушки с внутренней частью
    val earOffset = radius * 0.65f
    val earRadius = radius * 0.38f
    
    // Левое ушко
    drawCircle(
        color = Color.White,
        radius = earRadius,
        center = Offset(centerX - earOffset, centerY - earOffset)
    )
    drawCircle(
        color = Color.Black,
        radius = earRadius,
        center = Offset(centerX - earOffset, centerY - earOffset),
        style = Stroke(width = 3.dp.toPx())
    )
    drawCircle(
        color = SoftPink.copy(alpha = 0.3f),
        radius = earRadius * 0.6f,
        center = Offset(centerX - earOffset, centerY - earOffset)
    )
    
    // Правое ушко
    drawCircle(
        color = Color.White,
        radius = earRadius,
        center = Offset(centerX + earOffset, centerY - earOffset)
    )
    drawCircle(
        color = Color.Black,
        radius = earRadius,
        center = Offset(centerX + earOffset, centerY - earOffset),
        style = Stroke(width = 3.dp.toPx())
    )
    drawCircle(
        color = SoftPink.copy(alpha = 0.3f),
        radius = earRadius * 0.6f,
        center = Offset(centerX + earOffset, centerY - earOffset)
    )
    
    // Бантик с градиентом и блеском
    val bowY = centerY - radius * 1.15f
    val bowWidth = radius * 0.65f
    val bowHeight = radius * 0.45f
    
    // Основа бантика
    val bowColor = BrightPink
    
    // Левая часть бантика
    drawOval(
        brush = Brush.linearGradient(
            colors = listOf(
                bowColor,
                bowColor.copy(alpha = 0.8f)
            )
        ),
        topLeft = Offset(centerX - bowWidth, bowY),
        size = Size(bowWidth * 0.9f, bowHeight)
    )
    
    // Правая часть бантика
    drawOval(
        brush = Brush.linearGradient(
            colors = listOf(
                bowColor.copy(alpha = 0.8f),
                bowColor
            )
        ),
        topLeft = Offset(centerX + bowWidth * 0.1f, bowY),
        size = Size(bowWidth * 0.9f, bowHeight)
    )
    
    // Центр бантика
    drawOval(
        color = bowColor,
        topLeft = Offset(centerX - bowWidth * 0.2f, bowY + bowHeight * 0.1f),
        size = Size(bowWidth * 0.4f, bowHeight * 0.8f)
    )
    
    // Блеск на бантике
    val shineAlpha = (sin(bowShine * Math.PI * 2) * 0.5 + 0.5).toFloat()
    drawCircle(
        color = Color.White.copy(alpha = shineAlpha * 0.6f),
        radius = bowWidth * 0.15f,
        center = Offset(centerX - bowWidth * 0.4f, bowY + bowHeight * 0.3f)
    )
    
    // Контур бантика
    drawOval(
        color = Color.Black,
        topLeft = Offset(centerX - bowWidth, bowY),
        size = Size(bowWidth * 0.9f, bowHeight),
        style = Stroke(width = 2.5.dp.toPx())
    )
    drawOval(
        color = Color.Black,
        topLeft = Offset(centerX + bowWidth * 0.1f, bowY),
        size = Size(bowWidth * 0.9f, bowHeight),
        style = Stroke(width = 2.5.dp.toPx())
    )
    
    // Глаза (моргающие) с бликами
    val eyeRadius = radius * 0.12f * eyeScale
    val eyeY = centerY - radius * 0.15f
    
    // Левый глаз
    drawCircle(
        color = Color.Black,
        radius = eyeRadius,
        center = Offset(centerX - radius * 0.28f, eyeY)
    )
    if (eyeScale > 0.5f) {
        drawCircle(
            color = Color.White,
            radius = eyeRadius * 0.35f,
            center = Offset(centerX - radius * 0.25f, eyeY - eyeRadius * 0.3f)
        )
    }
    
    // Правый глаз
    drawCircle(
        color = Color.Black,
        radius = eyeRadius,
        center = Offset(centerX + radius * 0.28f, eyeY)
    )
    if (eyeScale > 0.5f) {
        drawCircle(
            color = Color.White,
            radius = eyeRadius * 0.35f,
            center = Offset(centerX + radius * 0.31f, eyeY - eyeRadius * 0.3f)
        )
    }
    
    // Носик (жёлтый овал с бликом)
    val noseY = centerY + radius * 0.08f
    drawOval(
        color = StarYellow,
        topLeft = Offset(centerX - radius * 0.12f, noseY),
        size = Size(radius * 0.24f, radius * 0.18f)
    )
    drawOval(
        color = Color.White.copy(alpha = 0.5f),
        topLeft = Offset(centerX - radius * 0.08f, noseY + radius * 0.03f),
        size = Size(radius * 0.08f, radius * 0.06f)
    )
    drawOval(
        color = Color.Black,
        topLeft = Offset(centerX - radius * 0.12f, noseY),
        size = Size(radius * 0.24f, radius * 0.18f),
        style = Stroke(width = 2.dp.toPx())
    )
    
    // Усики (с закруглениями)
    val whiskerY = centerY + radius * 0.22f
    val whiskerLength = radius * 0.45f
    
    // Левые усики
    drawLine(
        color = Color.Black,
        start = Offset(centerX - radius * 0.12f, whiskerY),
        end = Offset(centerX - whiskerLength, whiskerY - radius * 0.05f),
        strokeWidth = 2.5.dp.toPx(),
        cap = StrokeCap.Round
    )
    drawLine(
        color = Color.Black,
        start = Offset(centerX - radius * 0.12f, whiskerY + radius * 0.08f),
        end = Offset(centerX - whiskerLength * 0.9f, whiskerY + radius * 0.13f),
        strokeWidth = 2.5.dp.toPx(),
        cap = StrokeCap.Round
    )
    
    // Правые усики
    drawLine(
        color = Color.Black,
        start = Offset(centerX + radius * 0.12f, whiskerY),
        end = Offset(centerX + whiskerLength, whiskerY - radius * 0.05f),
        strokeWidth = 2.5.dp.toPx(),
        cap = StrokeCap.Round
    )
    drawLine(
        color = Color.Black,
        start = Offset(centerX + radius * 0.12f, whiskerY + radius * 0.08f),
        end = Offset(centerX + whiskerLength * 0.9f, whiskerY + radius * 0.13f),
        strokeWidth = 2.5.dp.toPx(),
        cap = StrokeCap.Round
    )
    
    // Маленькие сердечки по бокам
    drawTinyHeart(
        center = Offset(centerX - radius * 1.3f, centerY),
        size = radius * 0.15f,
        color = HeartRed.copy(alpha = 0.6f)
    )
    drawTinyHeart(
        center = Offset(centerX + radius * 1.3f, centerY),
        size = radius * 0.15f,
        color = HeartRed.copy(alpha = 0.6f)
    )
}

private fun DrawScope.drawTinyHeart(
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