package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject

import androidx.lifecycle.ViewModel
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.GroupDB
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.UserDB
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationsViewModel : ViewModel() {
    private val _friendNotifications = MutableStateFlow<List<UserDB>>(emptyList())
    val friendNotifications = _friendNotifications.asStateFlow()

    private val _groupNotifications = MutableStateFlow<List<GroupDB>>(emptyList())
    val groupNotifications = _groupNotifications.asStateFlow()

    fun updateNotifications(friendRequests: List<UserDB>, groups: List<GroupDB>) {
        _friendNotifications.value = friendRequests
        _groupNotifications.value = groups

    }
    fun deleteFriendRequest(userID: String) {
        val updatedRequests = _friendNotifications.value.filter { it.userID != userID }
        _friendNotifications.value = updatedRequests
    }

    fun deleteGroupRequest(groupID: String) {

        val updatedGroups = _groupNotifications.value.filter { it.groupID != groupID }
        _groupNotifications.value = updatedGroups
    }


}