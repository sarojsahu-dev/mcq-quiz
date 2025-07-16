package mcq.quiz.app.presentation.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import mcq.quiz.app.presentation.categorymodule.ModuleListScreen
import mcq.quiz.app.presentation.categorymodule.ModuleListViewModel
import mcq.quiz.app.presentation.quiz.QuizScreen
import mcq.quiz.app.presentation.quiz.QuizViewModel
import mcq.quiz.app.presentation.results.ResultsScreen
import mcq.quiz.app.presentation.results.ResultsViewModel

@Composable
fun QuizNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "module_list"
    ) {
        composable("module_list") {
            val moduleListViewModel: ModuleListViewModel = hiltViewModel()
            ModuleListScreen(
                onModuleSelected = { module ->
                    navController.navigate("quiz/${module.id}/${Uri.encode(module.questionsUrl)}")
                },
                viewModel = moduleListViewModel
            )
        }

        composable(
            route = "quiz/{moduleId}/{questionsUrl}",
            arguments = listOf(
                navArgument("moduleId") { type = NavType.StringType },
                navArgument("questionsUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val moduleId = backStackEntry.arguments?.getString("moduleId") ?: ""
            val questionsUrl = backStackEntry.arguments?.getString("questionsUrl") ?: ""

            val quizViewModel: QuizViewModel = hiltViewModel()

            LaunchedEffect(moduleId, questionsUrl) {
                quizViewModel.setModuleId(moduleId)
                quizViewModel.loadModuleQuestions(Uri.decode(questionsUrl))
            }

            QuizScreen(
                onNavigateToResults = {
                    navController.navigate("results/$moduleId") {
                        popUpTo("quiz/$moduleId/${Uri.encode(questionsUrl)}") { inclusive = false }
                    }
                },
                viewModel = quizViewModel
            )
        }

        composable(
            route = "results/{moduleId}",
            arguments = listOf(
                navArgument("moduleId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val moduleId = backStackEntry.arguments?.getString("moduleId") ?: ""

            val quizScreenEntry = navController.previousBackStackEntry
            val quizViewModel: QuizViewModel = hiltViewModel(quizScreenEntry ?: backStackEntry)
            val resultViewModel: ResultsViewModel = hiltViewModel()

            ResultsScreen(
                onFinish = {
                    navController.popBackStack("module_list", inclusive = false)
                },
                onRestartQuiz = {
                    navController.popBackStack()
                    quizViewModel.restartQuiz()
                },
                resultViewModel = resultViewModel,
                questions = quizViewModel.uiState.value.questions,
                moduleId = moduleId
            )
        }
    }
}