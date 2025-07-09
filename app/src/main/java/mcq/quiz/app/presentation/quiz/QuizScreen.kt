package mcq.quiz.app.presentation.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mcq.quiz.app.presentation.components.AnswerFeedback
import mcq.quiz.app.presentation.components.QuestionCard
import mcq.quiz.app.presentation.components.StreakBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    onNavigateToResults: () -> Unit,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isQuizCompleted) {
        if (uiState.isQuizCompleted) {
            onNavigateToResults()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mcq Quiz",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingScreen()
                }

                uiState.error != null -> {
                    ErrorScreen(
                        error = uiState.error!!,
                        onRetry = if (uiState.showRetry) viewModel::retry else null
                    )
                }

                uiState.currentQuestion != null -> {
                    QuizContent(
                        uiState = uiState,
                        onAnswerSelected = viewModel::selectAnswer,
                        onSkipQuestion = viewModel::skipQuestion
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Loading Quiz...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun ErrorScreen(
    error: String,
    onRetry: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Error: $error",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )

            if (onRetry != null) {
                Button(onClick = onRetry) {
                    Text("Retry")
                }
            }
        }
    }
}


@Composable
private fun QuizContent(
    uiState: QuizUiState,
    onAnswerSelected: (Int) -> Unit,
    onSkipQuestion: () -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TopSection(
                questionNumber = uiState.questionNumber,
                progress = uiState.progress,
                currentStreak = uiState.currentStreak,
                isStreakActive = uiState.isStreakActive
            )

            QuestionCard(
                question = uiState.currentQuestion!!,
                selectedAnswer = uiState.selectedAnswer,
                showAnswer = uiState.showAnswer,
                onAnswerSelected = onAnswerSelected,
                isProcessing = uiState.isAnswerProcessing,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            BottomSection(
                onSkipQuestion = onSkipQuestion,
                isEnabled = !uiState.showAnswer && !uiState.isAnswerProcessing
            )
        }

        AnimatedVisibility(
            visible = uiState.showAnswer,
            enter = fadeIn(animationSpec = tween(300)) + scaleIn(
                animationSpec = tween(300),
                initialScale = 0.8f
            ),
            exit = fadeOut(animationSpec = tween(200))
        ) {
            AnswerFeedback(
                isCorrect = uiState.selectedAnswer == uiState.currentQuestion?.correctOptionIndex,
                currentStreak = uiState.currentStreak,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun TopSection(
    questionNumber: String,
    progress: Float,
    currentStreak: Int,
    isStreakActive: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Question $questionNumber",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            StreakBadge(
                currentStreak = currentStreak,
                isActive = isStreakActive
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primaryContainer
        )
    }
}

@Composable
private fun BottomSection(
    onSkipQuestion: () -> Unit,
    isEnabled: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedButton(
            onClick = onSkipQuestion,
            enabled = isEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Skip")
        }
    }
}