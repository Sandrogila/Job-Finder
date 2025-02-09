package com.example.jobfinder.Services

import android.content.Context
import com.example.jobfinder.API.RetrofitClient
import com.example.jobfinder.dataClass.AuthenticationRequest
import com.example.jobfinder.dataClass.AuthenticationResponse
import com.example.jobfinder.dataClass.CompanyDTO
import com.example.jobfinder.dataClass.UserDto
import com.example.jobfinder.database.AppDatabase
import com.example.jobfinder.database.entities.UserEntity
import kotlinx.coroutines.runBlocking

class AuthRepository(private val context: Context) {

    private val userDao = AppDatabase.getDatabase(context).userDao()

    // Variável cache para o token (em memória)
    @Volatile
    private var cachedToken: String? = null

    // Salva os dados do usuário no Room e atualiza o cache
    private suspend fun saveUserLocally(authResponse: AuthenticationResponse,email: String) {
        val userEntity = UserEntity(
            token = authResponse.token,
            email = email
        )
        userDao.saveUser(userEntity)
        cachedToken = authResponse.token
    }

    // Registrar usuário
    suspend fun registerUser(userDto: UserDto): Result<AuthenticationResponse> {
        return try {
            if (userDto.name.isBlank() || userDto.email.isBlank() || userDto.password.isBlank()) {
                return Result.failure(Exception("Preencha todos os campos obrigatórios."))
            }
            val response = RetrofitClient.apiService.registerUser(userDto)
            if (response.isSuccessful) {
                val authResponse = response.body()!!
                saveUserLocally(authResponse,userDto.email)
                Result.success(authResponse)
            } else {
                Result.failure(Exception("Erro no registro: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Registrar empresa
    suspend fun registerCompany(companyDto: CompanyDTO): Result<AuthenticationResponse> {
        return try {
            if (companyDto.name.isBlank() || companyDto.email.isBlank() || companyDto.password.isBlank()) {
                return Result.failure(Exception("Preencha todos os campos obrigatórios."))
            }
            val response = RetrofitClient.apiService.registerCompany(companyDto)
            if (response.isSuccessful) {
                val authResponse = response.body()!!
                saveUserLocally(authResponse,companyDto.email)
                Result.success(authResponse)
            } else {
                Result.failure(Exception("Erro no registro: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Autenticar (login)
    suspend fun authenticate(email: String, password: String): Result<AuthenticationResponse> {
        return try {
            if (email.isBlank() || password.isBlank()) {
                return Result.failure(Exception("Preencha todos os campos obrigatórios."))
            }
            val request = AuthenticationRequest(email, password)
            val response = RetrofitClient.apiService.authenticate(request)
            if (response.isSuccessful) {
                val authResponse = response.body()!!
                saveUserLocally(authResponse,email)
                Result.success(authResponse)
            } else {
                Result.failure(Exception("Erro no login: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Recupera o usuário salvo no Room
    suspend fun getUser(): UserEntity? {
        return userDao.getUser()
    }

    // Método para uso no interceptor: retorna o token de forma síncrona
    fun getToken(): String? {
        // Se o token estiver em cache, retorna-o imediatamente
        if (!cachedToken.isNullOrBlank()) return cachedToken
        // Se não estiver em cache, faz uma chamada bloqueante para buscar do Room
        return runBlocking { userDao.getUser()?.token }
    }

    // Limpa os dados do usuário (logout)
    suspend fun clearUser() {
        userDao.clearUser()
        cachedToken = null
    }
}
