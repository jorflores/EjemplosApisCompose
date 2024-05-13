package com.example.ejemplosapis.model

data class User(
    val createdAt: String,
    val password: String,
    val phone: String,
    val role: String,
    val uid: Int,
    val updatedAt: String
)