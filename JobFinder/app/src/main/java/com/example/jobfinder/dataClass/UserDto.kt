package com.example.jobfinder.dataClass

data class UserDto(
    val name: String,
    val email: String,
    val password: String,
    val phone: String? = null,
    val resume: String? = null,
    val role: UserRole = UserRole.CANDIDATE
)