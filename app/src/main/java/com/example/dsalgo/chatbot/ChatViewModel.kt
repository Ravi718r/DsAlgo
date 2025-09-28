package com.example.dsalgo.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dsalgo.chatbot.data.model.ChatState
import com.example.dsalgo.chatbot.data.model.FilterCategory
import com.example.dsalgo.chatbot.data.model.Message
import com.example.dsalgo.chatbot.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _chatState = MutableStateFlow(ChatState())
    val chatState = _chatState.asStateFlow()

    init {
        loadMessageHistory()
    }

    fun sendMessage() {
        val currentInput = _chatState.value.currentInput.trim()
        if (currentInput.isBlank()) return

        // Add user message
        val userMessage = Message(
            text = currentInput,
            isFromUser = true,
            category = FilterCategory.detectCategory(currentInput)
        )

        viewModelScope.launch {
            repository.saveMessage(userMessage)
            updateMessages()
            clearInput()

            // Get AI response
            repository.sendMessage(currentInput)
                .onStart {
                    _chatState.value = _chatState.value.copy(isLoading = true, error = null)
                }
                .onCompletion {
                    _chatState.value = _chatState.value.copy(isLoading = false)
                }
                .catch { throwable ->
                    _chatState.value = _chatState.value.copy(
                        error = throwable.message ?: "Unknown error occurred",
                        isLoading = false
                    )
                }
                .collect { responseText ->
                    val aiMessage = Message(
                        text = responseText,
                        isFromUser = false,
                        category = FilterCategory.detectCategory(currentInput)
                    )
                    repository.saveMessage(aiMessage)
                    updateMessages()
                }
        }
    }

    fun updateInput(text: String) {
        _chatState.value = _chatState.value.copy(currentInput = text)
    }

    fun clearInput() {
        _chatState.value = _chatState.value.copy(currentInput = "")
    }

    fun toggleFilter(category: FilterCategory) {
        val currentFilters = _chatState.value.enabledFilters.toMutableSet()
        if (currentFilters.contains(category)) {
            currentFilters.remove(category)
        } else {
            currentFilters.add(category)
        }
        _chatState.value = _chatState.value.copy(enabledFilters = currentFilters)
        updateMessagesSync() // Re-filter messages
    }

    fun showFilterDialog() {
        _chatState.value = _chatState.value.copy(showFilterDialog = true)
    }

    fun hideFilterDialog() {
        _chatState.value = _chatState.value.copy(showFilterDialog = false)
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearHistory()
            _chatState.value = ChatState(enabledFilters = _chatState.value.enabledFilters)
        }
    }

    fun dismissError() {
        _chatState.value = _chatState.value.copy(error = null)
    }

    private fun loadMessageHistory() {
        viewModelScope.launch {
            updateMessages()
        }
    }

    private suspend fun updateMessages() {
        val allMessages = repository.getMessageHistory()
        val filteredMessages = allMessages.filter { message ->
            // Show user messages always, filter AI responses based on enabled filters
            if (message.isFromUser) {
                true
            } else {
                message.category?.let { category ->
                    _chatState.value.enabledFilters.contains(category)
                } ?: true
            }
        }
        _chatState.value = _chatState.value.copy(messages = filteredMessages)
    }

    private fun updateMessagesSync() {
        viewModelScope.launch {
            updateMessages()
        }
    }
}