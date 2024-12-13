package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.AuthViewModel
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.UserDB
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.SearchManager
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalContext
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.R
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.DatabaseProvider
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.DatabaseProvider
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

@Composable
fun FriendsContent(){

}

@Composable
fun Friends(changeAddInProgress: () -> Unit, navController: NavController){
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val db = DatabaseProvider.getDatabase(context)
    var friends by remember { mutableStateOf<List<UserDB>>(
        listOf()
    ) }
    var displayedFriends by remember { mutableStateOf<List<UserDB>>(
        listOf()
    ) }
    LaunchedEffect(Unit) {
        friends = db.movieDao().getFriends()
        displayedFriends = friends
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(modifier = Modifier
            .align(Alignment.TopCenter)
            .fillMaxSize()) {
            SearchHeader(
                title = "Friends",
                rightButtonHandler =  {
                    changeAddInProgress()
                },
                searchHandler = {
                    if (it != "" )
                    coroutineScope.launch {
                        displayedFriends = db.movieDao().prefixSearch(it)
                    }
                    else displayedFriends = friends
                },
                searchLabel = "Search Friends",
                rightButtonIcon = Icons.Filled.Add,
            )
            OutlinedButton(
                onClick = {
                    coroutineScope.launch {
                        val users = listOf(getsampleUser("James"), getsampleUser("Jane"), getsampleUser("Bob"))
                        for (user in users)  db.movieDao().insertUser(user)
                    }
                }
            ) {
                Text("Put friends to DB")
            }
            UserSearchResultsList(displayedFriends, onAddFriend = {}, modifier = Modifier.fillMaxSize())
        }
        FooterNavigation(
            modifier = Modifier.align(Alignment.BottomCenter),
            navController = navController
        )
    }
}


@Composable
fun AddFriend(
    changeAddInProgress: () -> Unit,
    searchManager: SearchManager
){
    val coroutineScope = rememberCoroutineScope()
    var friends by remember { mutableStateOf<List<UserDB>>(listOf()) }
    Column {
        SearchHeader(
            title = "Add Friend",
            rightButtonHandler =  {
                changeAddInProgress()
            },
            searchHandler = {
                coroutineScope.launch {
                    val response = searchManager.searchUsers(it, "algoliaUsers")
                    if (response.isNotEmpty()) {
                        friends = response.map { hit ->
                            val properties = hit.additionalProperties
                            val profilePicture = properties?.get("Profile_Picture")
                            val sessions = properties?.get("Sessions")
                            val movies = properties?.get("Movies")
                            val username = properties?.get("Username")
                            val favGenre = properties?.get("favGenre")
                            val id = properties?.get("id")
                            val email =  properties?.get("email")
                            val firstName = properties?.get("FirstName")
                            val lastName = properties?.get("LastName")
                            UserDB(
                                userName = if (username != null) Json.decodeFromJsonElement<String>(username) else "",
                                favoriteGenre = if (favGenre != null) Json.decodeFromJsonElement<String>(favGenre) else "",
                                userID = if (id != null) Json.decodeFromJsonElement<String>(id) else "",
                                email = if (email != null) Json.decodeFromJsonElement<String>(email) else "",
                                firstName = if (firstName != null) Json.decodeFromJsonElement<String>(firstName) else "",
                                lastName = if (lastName != null) Json.decodeFromJsonElement<String>(lastName) else "",
                                profilePicture =  R.drawable.joker, // if (profilePicture != null ) Json.decodeFromJsonElement<Int>(profilePicture) else 0,
                                pending = false,
                                self = 0,
                                movies = if (sessions != null ) Json.decodeFromJsonElement<List<String>>(sessions) else listOf(),
                                sessions = if (movies != null ) Json.decodeFromJsonElement<List<String>>(movies) else listOf(),
                                )
                        }

                    }
                }
            }, // Firebase search here
            searchLabel = "Find Friend to Add",
            rightButtonIcon = Icons.Filled.Close
        )
        UserSearchResultsList(friends, onAddFriend = {}, modifier = Modifier.fillMaxSize())
    }

}



@Composable
fun FriendsPage(modifier : Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){
    var addInProgress by remember { mutableStateOf(false) }
    val changeAddInProgress: () -> Unit = { addInProgress = !addInProgress }
    var searchManager = SearchManager()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            searchManager.searchUsers("aleks", "algoliaUsers")
        }
    }

    if (!addInProgress){
        Friends(changeAddInProgress = changeAddInProgress, navController = navController)
    }
    else{
        AddFriend(changeAddInProgress = changeAddInProgress, searchManager = searchManager)
    }
}





@Composable
fun UserSearchResultCard(
    user: UserDB,
    onAddFriendClick: (UserDB) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.elevatedCardElevation(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Picture
            Image(
                painter = painterResource(id = user.profilePicture),
                contentDescription = "${user.firstName} ${user.lastName} Profile Picture",
                modifier = Modifier
                    .size(55.dp)
                    .clip(RoundedCornerShape(50)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // User Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "@${user.userName}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Favorite genre: ${user.favoriteGenre}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Add Friend Button
            IconButton(
                onClick = { onAddFriendClick(user) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add ${user.userName} as a friend",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Composable
fun UserSearchResultsList(
    users: List<UserDB>,
    onAddFriend: (UserDB) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(users) { user ->
            UserSearchResultCard(
                user = user,
                onAddFriendClick = onAddFriend
            )
        }
    }
}

val getsampleUser: (String) -> UserDB =
{
    UserDB(
        userID = it,
        userName = it,
        profilePicture = R.drawable.joker,
        movies = listOf("Interstellar", "Inception", "The Matrix"),
        sessions = listOf("session1", "session2"),
        email = "johndoe@example.com",
        firstName = it,
        lastName = "Doe",
        favoriteGenre = "Sci-Fi",
        pending = false,
        self = 0
    )
}

@Preview
@Composable
fun FriendListPreview() {


    val users = listOf(getsampleUser("James"), getsampleUser("Jane"), getsampleUser("Bob"))
    UserSearchResultsList(users, onAddFriend = {})
}


//@Preview
//@Composable
//fun FriendsPagePreview(){
//    FriendsPage()
//}

