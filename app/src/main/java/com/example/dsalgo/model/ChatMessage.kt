package com.example.dsalgo.model

data class ChatMessage(
    val id: String = "",
    val message: String = "",
    val isUser: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)

data class ChatConversation(
    val messages: List<ChatMessage> = emptyList()
)

