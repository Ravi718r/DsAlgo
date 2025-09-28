package com.example.dsalgo.chatbot.data.repository

import com.example.dsalgo.chatbot.data.model.FilterCategory
import com.example.dsalgo.chatbot.data.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun sendMessage(text : String) : Flow<String>
    suspend fun getMessageHistory() : List<Message>
    suspend fun saveMessage(message: Message)
    suspend fun clearHistory()
    fun isMessageRelevant(text: String, enabledFilters: Set<FilterCategory>): Boolean

}