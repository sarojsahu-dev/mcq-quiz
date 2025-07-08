package mcq.quiz.app.presentation.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnswerFeedback(
    isCorrect: Boolean,
    currentStreak: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        )
    )

    val particleOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = modifier
            .background(
                if (isCorrect) Color.Black.copy(alpha = 0.3f)
                else Color.Red.copy(alpha = 0.2f)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isCorrect) {
            repeat(8) { index ->
                val angle = (index * 45f)
                val offsetX = kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat() * particleOffset
                val offsetY = kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat() * particleOffset

                Box(
                    modifier = Modifier
                        .offset(x = offsetX.dp, y = offsetY.dp)
                        .size(8.dp)
                        .background(
                            Color(0xFFFFD700),
                            CircleShape
                        )
                        .scale(scale * 0.5f)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
                contentDescription = if (isCorrect) "Correct" else "Incorrect",
                tint = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFE57373),
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        Color.White,
                        CircleShape
                    )
                    .padding(20.dp)
                    .scale(scale)
            )

            // Feedback text
            Text(
                text = if (isCorrect) {
                    when {
                        currentStreak >= 5 -> "🔥 ON FIRE! 🔥"
                        currentStreak >= 3 -> "⚡ STREAK! ⚡"
                        else -> "✅ Correct!"
                    }
                } else {
                    "❌ Incorrect"
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // Streak celebration
            if (isCorrect && currentStreak >= 3) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFD700)
                    ),
                    modifier = Modifier.scale(scale * 0.8f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (currentStreak >= 5) Icons.Default.Whatshot else Icons.Default.Star,
                            contentDescription = "Streak",
                            tint = Color.Black
                        )
                        Text(
                            text = "$currentStreak in a row!",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(5) { index ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                if (index < (currentStreak.coerceAtMost(5))) {
                                    Color(0xFFFFD700)
                                } else {
                                    Color.White.copy(alpha = 0.3f)
                                },
                                CircleShape
                            )
                    )
                }
            }
        }
    }
}