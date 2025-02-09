package com.example.jobfinder.viewScreen.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jobfinder.viewModel.AuthViewModel
import com.example.jobfinder.viewModel.JobViewModel
import com.example.jobfinder.viewScreen.LoginScreen
import com.example.jobfinder.viewScreen.RegisterScreen
import com.example.jobfinder.viewScreen.SplashScreen
import com.example.jobfinder.viewScreen.cards.EditJobScreen
import com.example.jobfinder.viewScreen.cards.JobDetailsScreen
import com.example.jobfinder.viewScreen.dashboard.UserDashboardScreen
import com.example.jobfinder.viewScreen.dashboard.CompanyDashboardScreen

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Navigation(jobViewModel: JobViewModel, authViewModel: AuthViewModel,startDestination: String) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable("splash") {
            SplashScreen(navController = navController)
        }
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }
        composable("user_dashboard") {
            UserDashboardScreen(navController = navController, jobViewModel = jobViewModel)
        }
        composable("company_dashboard") {
            CompanyDashboardScreen(navController = navController, jobViewModel = jobViewModel)
        }
        composable(
            route = "EditeJob/{jobId}",
            arguments = listOf(navArgument("jobId") { type = NavType.LongType })
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getLong("jobId")
            val jobs = jobViewModel.jobs.collectAsState().value
            val job = jobs.find { it.id == jobId }

            EditJobScreen(
                job = job,
                onSave = { navController.popBackStack() },
                onDelete = {
                    jobId?.let { jobViewModel.deleteJob(it) }
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() },
                jobViewModel = jobViewModel
            )
        }
        composable("EditeJob") {
            EditJobScreen(
                job = null, // Criando uma nova vaga
                onSave = { navController.popBackStack() },
                onCancel = { navController.popBackStack() },
                jobViewModel = jobViewModel
            )
        }

        composable(
            route = "jobDetails/{jobId}",
            arguments = listOf(navArgument("jobId") { type = NavType.LongType })
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getLong("jobId")
            val jobs = jobViewModel.jobs.collectAsState().value
            val job = jobs.find { it.id == jobId }
            if (job != null) {
                JobDetailsScreen(
                    job = job,
                    navController = navController,
                    jobViewModel = jobViewModel
                )
            } else {
                // Exibe uma mensagem de erro ou tela de loading\n        Text(\"Vaga não encontrada\", modifier = Modifier.padding(16.dp))\n    }\n}
            }
        }

    }
}
