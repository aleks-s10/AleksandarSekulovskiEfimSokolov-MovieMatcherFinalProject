package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject

import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.room.util.EMPTY_STRING_ARRAY
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.FirestoreUsersDB
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.DatabaseProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlin.String
import kotlin.collections.listOf

class AuthViewModel : ViewModel() {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    var currentUser = ""

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState


    init {
        checkAuthStatus()
    }

    fun checkAuthStatus(){
        if(auth.currentUser==null){
            _authState.value = AuthState.Unauthenticated
        }else{
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email : String, password : String) {

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Please put values in for email and password")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if (task.isSuccessful){
                    currentUser = email
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Sorry, something went wrong")
                }
            }
    }

    fun checkUserInfoInFirestore(
        Username: String
    ): Boolean {

        var worked = true

        val db = FirebaseFirestore.getInstance()
        // Check if the username already exists
        db.collection("users")
            .whereEqualTo("Username", Username)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    worked = false
                    // Username exists, log error
                    Log.e("FIRESTORE", "Error: Username already exists")
                }
            }
            .addOnFailureListener { e ->
                Log.e("FIRESTORE", "Error checking username existence", e)
            }
        return worked
    }

    fun saveUserInfoToFirestore(
        id: String,
        Movies: List<String>,
        Profile_Picture: Int = 0,
        Sessions: List<String>,
        email: String,
        Username: String = "",
        firstName: String = "",
        lastName: String = "",
        favGenre: String = ""
    ): Boolean {
            val user = FirestoreUsersDB(
                id = id,
                Movies = Movies,
                Profile_Picture = Profile_Picture,
                Sessions = Sessions,
                email = email,
                Username = Username,
                FirstName = firstName,
                LastName = lastName,
                favGenre = favGenre
            )

            val data = hashMapOf(
                "id" to user.id,
                "Movies" to user.Movies,
                "Profile_Picture" to user.Profile_Picture,
                "Sessions" to user.Sessions,
                "email" to user.email,
                "Username" to user.Username,
                "FirstName" to user.FirstName,
                "LastName" to user.LastName,
                "favGenre" to user.favGenre
            )
            var worked = true

            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(user.id)
                .set(data)
                .addOnFailureListener { e ->
                    Log.e("FIRESTORE", "Error checking username existence", e)
                    worked = false
                }
        return worked
    }

    fun signup(
        email : String,
        password : String,
        username: String,
        profilePicture: Int,
        firstName: String,
        lastName: String,
        favoriteGenre: String
    ) {

        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            _authState.value = AuthState.Error("Please put values in for all fields")
            return
        }

        if (checkUserInfoInFirestore(
                Username = username,
        )) {
            _authState.value = AuthState.Loading
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveUserInfoToFirestore(
                            id = email,
                            Movies = listOf(),
                            Profile_Picture = profilePicture,
                            Sessions = listOf(),
                            email = email,
                            Username = username,
                            firstName = firstName,
                            lastName = lastName,
                            favGenre = favoriteGenre
                        )
                        _authState.value = AuthState.Authenticated
                    } else {
                        _authState.value = AuthState.Error(
                            task.exception?.message ?: "Sorry, something went wrong"
                        )
                    }
                }
        } else {
            _authState.value = AuthState.Error(
                "Username already taken"
            )
        }
    }


    fun signout(navController: NavController){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
        currentUser = ""
        navController.navigate("login")
    }

}

sealed class AuthState{
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()


}