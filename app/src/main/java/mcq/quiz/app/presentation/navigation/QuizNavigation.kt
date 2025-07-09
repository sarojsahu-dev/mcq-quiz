package mcq.quiz.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import mcq.quiz.app.presentation.quiz.QuizScreen
import mcq.quiz.app.presentation.quiz.QuizViewModel
import mcq.quiz.app.presentation.results.ResultsScreen
import mcq.quiz.app.presentation.results.ResultsViewModel

@Composable
fun QuizNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "quiz"
    ) {
        composable("quiz") {
            val quizViewModel: QuizViewModel = hiltViewModel()
            QuizScreen(
                onNavigateToResults = {
                    navController.navigate("results") {
                        popUpTo("quiz") { inclusive = false }
                    }
                },
                viewModel = quizViewModel
            )
        }

        composable("results") {
            val quizScreenViewModel: QuizViewModel = hiltViewModel(
                navController.getBackStackEntry("quiz")
            )
            val resultViewModel: ResultsViewModel = hiltViewModel()

            ResultsScreen(
                onRestartQuiz = {
                    navController.popBackStack("quiz", inclusive = false)
                    quizScreenViewModel.restartQuiz()
                },
                resultViewModel = resultViewModel,
                questions = quizScreenViewModel.uiState.value.questions
            )
        }
    }
}