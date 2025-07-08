package mcq.quiz.app.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun OptionButton(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isWrong: Boolean,
    showAnswer: Boolean,
    onClick: () -> Unit,
    enabled: Boolean,
    isProcessing: Boolean = false,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        isCorrect && showAnswer -> Color(0xFF4CAF50)
        isWrong && showAnswer -> Color(0xFFE57373)
        isSelected && !showAnswer -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surface
    }

    val contentColor = when {
        isCorrect && showAnswer -> Color.White
        isWrong && showAnswer -> Color.White
        isSelected && !showAnswer -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurface
    }

    val borderColor = when {
        isCorrect && showAnswer -> Color(0xFF4CAF50)
        isWrong && showAnswer -> Color(0xFFE57373)
        isSelected && !showAnswer -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }

    val animatedBackgroundColor by animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = tween(300)
    )

    val animatedContentColor by animateColorAsState(
        targetValue = contentColor,
        animationSpec = tween(300)
    )

    val animatedBorderColor by animateColorAsState(
        targetValue = borderColor,
        animationSpec = tween(300)
    )

    val infiniteTransition = rememberInfiniteTransition()
    val processingAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        )
    )

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .then(
                if (isProcessing) {
                    Modifier.alpha(processingAlpha)
                } else Modifier
            ),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = animatedBackgroundColor,
            contentColor = animatedContentColor
        ),
        border = BorderStroke(
            width = 2.dp,
            color = animatedBorderColor
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f).padding(4.dp)
            )

            if (isProcessing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = animatedContentColor
                )
            }
        }
    }
}