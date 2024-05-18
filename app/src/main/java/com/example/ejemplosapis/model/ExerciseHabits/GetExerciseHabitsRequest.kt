package com.example.ejemplosapis.model.ExerciseHabits

data class GetExerciseHabitsRequest(
    val endDate: String,
    val formatted: String,
    val startDate: String,
    val uid: Int
)