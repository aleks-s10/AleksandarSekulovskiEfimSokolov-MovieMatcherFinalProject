package com.example.aleksandarsekulov_moviematcherfinalproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.NotificationsViewModel

class NotificationsViewModelFactory(
    private val notificationsViewModel: NotificationsViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return notificationsViewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}