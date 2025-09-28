package com.example.dsalgo.chatbot.data.model

import java.util.UUID

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val text: String,  // ✅ ADDED: This was missing!
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val category: FilterCategory? = null
)
