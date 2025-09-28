package com.example.dsalgo.chatbot.data.model

data class ChatState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentInput: String = "",
    val enabledFilters: Set<FilterCategory> = FilterCategory.values().toSet(),
    val showFilterDialog: Boolean = false
)