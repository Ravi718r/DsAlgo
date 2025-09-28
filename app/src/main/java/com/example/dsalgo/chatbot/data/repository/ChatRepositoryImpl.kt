package com.example.dsalgo.chatbot.data.repository

import com.example.dsalgo.chatbot.data.model.FilterCategory
import com.example.dsalgo.chatbot.data.model.Message
import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor() : ChatRepository {

    // ✅ FIXED: Variable name matches usage
    private val generativeModel = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-2.0-flash-exp")

    // ✅ FIXED: Changed from mutableListOf<List<Message>>() to mutableListOf<Message>()
    private val messageHistory = mutableListOf<Message>()

    private val systemPrompt = """
        You are an AI assistant specialized in helping students with technical concepts in:
        - Data Structures & Algorithms (DSA)
        - Computer Networks (CN) 
        - Operating Systems (OS)
        - Object-Oriented Programming (OOP)
        
        Rules:
        1. ONLY answer questions related to DSA, CN, OS, or OOP
        2. If a question is not related to these topics, politely redirect the user
        3. Keep answers concise but comprehensive
        4. Provide examples when helpful
        5. Use simple language suitable for students
        6. If asked about other topics, respond: "I can only help with DSA, Computer Networks, Operating Systems, or Object-Oriented Programming topics. Please ask something related to these subjects."
        
        Format your responses clearly with:
        - Clear explanations
        - Bullet points for lists
        - Code examples when relevant
        - Step-by-step processes when applicable
    """.trimIndent()

    override suspend fun sendMessage(text: String): Flow<String> = flow {
        try {
            val category = FilterCategory.detectCategory(text)
            if (category == null) {
                emit("I can only help with DSA, Computer Networks, Operating Systems, or Object-Oriented Programming topics. Please ask something related to these subjects.")
                return@flow
            }

            // ✅ FIXED: Removed extra backslash
            val prompt = "$systemPrompt\n\nUser Question: $text"
            val response = generativeModel.generateContent(prompt)
            val responseText = response.text ?: "Sorry, I couldn't generate a response."

            emit(responseText)
        } catch (e: Exception) {
            emit("Sorry, there was an error processing your request: ${e.message}")
        }
    }

    override suspend fun getMessageHistory(): List<Message> {
        return messageHistory.toList()
    }

    override suspend fun saveMessage(message: Message) {
        // ✅ FIXED: Now correctly adds single Message to the list
        messageHistory.add(message)

        if (messageHistory.size > 100) {
            messageHistory.removeAt(0)
        }
    }

    override suspend fun clearHistory() {
        messageHistory.clear()
    }

    override fun isMessageRelevant(text: String, enabledFilters: Set<FilterCategory>): Boolean {
        return FilterCategory.isRelevantToCategories(text, enabledFilters)
    }
}
