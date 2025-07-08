package mcq.quiz.app.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StreakBadge(
    currentStreak: Int,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()

    // Different animations based on streak level
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = when {
            currentStreak >= 5 -> 1.15f  // Super streak
            isActive -> 1.1f              // Active streak
            else -> 1f                    // Normal
        },
        animationSpec = infiniteRepeatable(
            animation = tween(if (currentStreak >= 5) 600 else 800),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Fire effect for high streaks
    val fireRotation by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(400),
            repeatMode = RepeatMode.Reverse
        )
    )

    val (backgroundColor, contentColor, icon) = when {
        currentStreak >= 5 -> Triple(
            Color(0xFFFF5722), // Deep orange for fire
            Color.White,
            Icons.Default.Whatshot
        )
        isActive -> Triple(
            Color(0xFFFFD700), // Gold
            Color.Black,
            Icons.Default.Star
        )
        else -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            Icons.Default.Star
        )
    }

    Box(modifier = modifier) {
        if (currentStreak >= 5) {
            // Particle effects
            repeat(3) { index ->
                val particleScale by infiniteTransition.animateFloat(
                    initialValue = 0.5f,
                    targetValue = 1.2f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, delayMillis = index * 200),
                        repeatMode = RepeatMode.Reverse
                    )
                )

                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .offset(
                            x = (index * 8 - 8).dp,
                            y = (-index * 4).dp
                        )
                        .scale(particleScale)
                        .background(
                            Color(0xFFFFD700),
                            CircleShape
                        )
                )
            }
        }

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(backgroundColor)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .scale(scale),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Streak",
                tint = contentColor,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "$currentStreak",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )

            if (currentStreak >= 3) {
                Text(
                    text = when {
                        currentStreak >= 7 -> "🔥"
                        currentStreak >= 5 -> "⚡"
                        else -> "✨"
                    },
                    fontSize = 14.sp
                )
            }
        }
    }
}
