package com.vishesh.codinglearning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vishesh.codinglearning.data.UserPreferencesRepository
import com.vishesh.codinglearning.data.api.MockCompilerApi
import com.vishesh.codinglearning.data.repository.*
import com.vishesh.codinglearning.domain.usecase.*
import com.vishesh.codinglearning.data.model.ProgrammingLanguage
import com.vishesh.codinglearning.ui.home.HomeScreen
import com.vishesh.codinglearning.ui.home.HomeViewModel
import com.vishesh.codinglearning.ui.language.LanguageSelectionScreen
import com.vishesh.codinglearning.ui.language.LanguageSelectionViewModel
import com.vishesh.codinglearning.ui.level.LevelSelectionScreen
import com.vishesh.codinglearning.ui.level.LevelViewModel
import com.vishesh.codinglearning.ui.mcq.McqScreen
import com.vishesh.codinglearning.ui.mcq.McqViewModel
import com.vishesh.codinglearning.ui.problem.ProblemDetailScreen
import com.vishesh.codinglearning.ui.problem.ProblemListScreen
import com.vishesh.codinglearning.ui.problem.ProblemViewModel
import com.vishesh.codinglearning.ui.result.ResultScreen
import com.vishesh.codinglearning.ui.theme.CodingLearningTheme
import com.vishesh.codinglearning.ui.trycode.TryCodeScreen
import com.vishesh.codinglearning.ui.trycode.TryCodeViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var levelRepository: LevelRepository
    private lateinit var mcqRepository: McqRepository
    private lateinit var compilerRepository: CompilerRepository
    private lateinit var problemRepository: ProblemRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize repositories
        userPreferencesRepository = UserPreferencesRepository(applicationContext)
        levelRepository = LevelRepositoryImpl()
        mcqRepository = McqRepositoryImpl()
        compilerRepository = CompilerRepositoryImpl(MockCompilerApi())
        problemRepository = ProblemRepositoryImpl()
        
        setContent {
            CodingLearningTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
    
    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()
        
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") {
                val viewModel = HomeViewModel(userPreferencesRepository)
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToLanguageSelection = {
                        navController.navigate("language_selection")
                    }
                )
            }
            
            composable("language_selection") {
                val viewModel = LanguageSelectionViewModel(userPreferencesRepository)
                LanguageSelectionScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToLevelSelection = {
                        navController.navigate("level_selection")
                    }
                )
            }
            
            composable("level_selection") {
                val viewModel = LevelViewModel(levelRepository, userPreferencesRepository)
                LevelSelectionScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToMcq = { levelNumber ->
                        navController.navigate("mcq/$levelNumber")
                    }
                )
            }
            
            composable(
                route = "mcq/{levelId}",
                arguments = listOf(navArgument("levelId") { type = NavType.IntType })
            ) { backStackEntry ->
                val levelId = backStackEntry.arguments?.getInt("levelId") ?: 1
                val viewModel = McqViewModel(
                    mcqRepository = mcqRepository,
                    userPreferencesRepository = userPreferencesRepository,
                    submitMcqAnswerUseCase = SubmitMcqAnswerUseCase(userPreferencesRepository),
                    useHintUseCase = UseHintUseCase(userPreferencesRepository),
                    levelId = levelId
                )
                McqScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToResult = { correct, total ->
                        navController.navigate("result/$correct/$total") {
                            popUpTo("mcq/$levelId") { inclusive = true }
                        }
                    }
                )
            }
            
            composable(
                route = "result/{correct}/{total}",
                arguments = listOf(
                    navArgument("correct") { type = NavType.IntType },
                    navArgument("total") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val correct = backStackEntry.arguments?.getInt("correct") ?: 0
                val total = backStackEntry.arguments?.getInt("total") ?: 1
                ResultScreen(
                    correctAnswers = correct,
                    totalQuestions = total,
                    onContinue = {
                        // TODO: Show interstitial ad here
                        navController.navigate("try_code")
                    }
                )
            }
            
            composable("try_code") {
                // Get selected language
                val language = runBlocking {
                    userPreferencesRepository.selectedLanguageFlow.first()
                } ?: ProgrammingLanguage.PYTHON
                
                val viewModel = TryCodeViewModel(
                    runCodeUseCase = RunCodeUseCase(compilerRepository),
                    language = language
                )
                TryCodeScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToProblems = {
                        navController.navigate("problem_list")
                    }
                )
            }
            
            composable("problem_list") {
                val language = runBlocking {
                    userPreferencesRepository.selectedLanguageFlow.first()
                } ?: ProgrammingLanguage.PYTHON
                
                val viewModel = ProblemViewModel(
                    problemRepository = problemRepository,
                    submitProblemUseCase = SubmitProblemUseCase(
                        compilerRepository,
                        userPreferencesRepository
                    ),
                    language = language
                )
                ProblemListScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToProblemDetail = {
                        navController.navigate("problem_detail")
                    }
                )
            }
            
            composable("problem_detail") {
                val language = runBlocking {
                    userPreferencesRepository.selectedLanguageFlow.first()
                } ?: ProgrammingLanguage.PYTHON
                
                val viewModel = ProblemViewModel(
                    problemRepository = problemRepository,
                    submitProblemUseCase = SubmitProblemUseCase(
                        compilerRepository,
                        userPreferencesRepository
                    ),
                    language = language
                )
                ProblemDetailScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
