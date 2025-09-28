package com.example.dsalgo.model

data class QuizModel(
    val subject: String = "",
    val total_questions: Int = 0,
    val difficulty_distribution: DifficultyDistribution = DifficultyDistribution(),
    val questions: List<QuizQuestion> = emptyList()
)

data class DifficultyDistribution(
    val easy: Int = 0,
    val medium: Int = 0,
    val hard: Int = 0
)

data class QuizQuestion(
    val id: String = "",
    val question: String = "",
    val options: List<String> = emptyList(),
    val correct_answer: String = "",
    val level: String = "",
    val explanation: String = ""
)

