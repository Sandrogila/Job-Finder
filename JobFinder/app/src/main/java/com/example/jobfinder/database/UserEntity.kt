package com.example.jobfinder.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(

    @PrimaryKey val token: String,
    val email: String // Usamos o email como chave primária
    // Usamos o token como identificador único

)
