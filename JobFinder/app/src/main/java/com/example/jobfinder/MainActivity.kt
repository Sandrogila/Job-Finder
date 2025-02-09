package com.example.jobfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.jobfinder.API.RetrofitClient
import com.example.jobfinder.Services.AuthRepository
import com.example.jobfinder.Services.JobRepository
import com.example.jobfinder.ui.theme.JobFinderTheme
import com.example.jobfinder.utils.SessionManager
import com.example.jobfinder.viewModel.AuthViewModel
import com.example.jobfinder.viewModel.AuthViewModelFactory
import com.example.jobfinder.viewModel.JobViewModel
import com.example.jobfinder.viewModel.JobViewModelFactory
import com.example.jobfinder.viewScreen.navigation.Navigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o RetrofitClient (que agora usa o AuthRepository internamente)
        RetrofitClient.init(this)

        enableEdgeToEdge()

        // Inicializa o JobRepository usando o apiService do RetrofitClient
        val jobRepository = JobRepository(RetrofitClient.apiService)
        val jobViewModelFactory = JobViewModelFactory(jobRepository)
        val jobViewModel = ViewModelProvider(this, jobViewModelFactory)[JobViewModel::class.java]

        // Inicializa o AuthRepository e AuthViewModel (que usa Room para persistir o usuário)
        val authRepository = AuthRepository(this)
        val authViewModelFactory = AuthViewModelFactory(authRepository)
        val authViewModel = ViewModelProvider(this, authViewModelFactory)[AuthViewModel::class.java]

        setContent {
            JobFinderTheme {
                val sessionManager = SessionManager(this)
                val userType = sessionManager.getUserType()

                val startDestination = if (userType != null) {
                    when (userType) {
                        "user" -> "user_dashboard"
                        "company" -> "company_dashboard"
                        else -> "splash"
                    }
                } else {
                    "splash" // Se não houver usuário salvo, ir para login
                }

                Navigation(jobViewModel, authViewModel, startDestination)
            }
        }
    }
}
