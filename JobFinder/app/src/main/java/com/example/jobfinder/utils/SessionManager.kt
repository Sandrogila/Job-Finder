package com.example.jobfinder.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.jobfinder.database.AppDatabase
import com.example.jobfinder.database.entities.UserEntity
import kotlinx.coroutines.runBlocking

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    // Salva o tipo de usuário (User ou Company)
    fun saveUserType(userType: String) {
        prefs.edit().putString("USER_TYPE", userType).apply()
    }

    // Obtém o tipo de usuário salvo
    fun getUserType(): String? {
        return prefs.getString("USER_TYPE", null)
    }

    // Limpa a sessão ao deslogar
    fun clearSession() {
        prefs.edit().clear().apply()
    }
    fun logout() {
        clearSession() // Apaga os dados salvos
    }
    private val userDao = AppDatabase.getDatabase(context).userDao()

    // Recupera o usuário salvo no banco de dados Room (bloqueante para simplificar; em produção, use coroutines corretamente)
    fun getUser(): UserEntity? = runBlocking { userDao.getUser() }

    // Limpa os dados do usuário (logout)
    fun logoutt() = runBlocking { userDao.clearUser() }
}
