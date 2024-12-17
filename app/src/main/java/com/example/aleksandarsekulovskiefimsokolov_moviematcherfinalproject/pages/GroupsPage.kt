package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.AuthViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.R
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.UserDB
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.DatabaseProvider
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.FirebaseSessions
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.GroupDB
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.launch


@Composable
fun GroupsContent(){

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserToasts(
    users: Set<UserDB>,
    onClick: (UserDB)  -> () -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        users.forEach {
            UserToast(
                user = it,
                onClick = onClick
            )
        }
    }
}


@Composable
fun GroupsList(
    groups: List<GroupDB>,
    onStartSessionClick: (GroupDB) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(groups) { group ->
            GroupPreview(
                groupName = group.sessionName,
                memberProfilePictures = listOf(R.drawable.joker),
                onStartSessionClick = { onStartSessionClick(group) }
            )
        }
    }
}


@Composable
fun Groups(changeAddInProgress: () -> Unit, navController: NavController){
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val db = DatabaseProvider.getDatabase(context)
    var groups by remember { mutableStateOf<List<GroupDB>>(
        listOf()
    ) }
    LaunchedEffect(Unit) {
        groups = db.movieDao().getGroups()
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            SearchHeader(
                title = "Groups",
                rightButtonHandler =  {
                    changeAddInProgress()
                },
                searchHandler = {},
                searchLabel = "Search Groups",
                rightButtonIcon = Icons.Filled.Add,
            )
            GroupsList(groups, onStartSessionClick = {
                navController.navigate("session/${it.groupID}")
            })
        }
        FooterNavigation(
            modifier = Modifier.align(Alignment.BottomCenter),
            navController = navController
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGroup(
    changeAddInProgress: () -> Unit,
    authViewModel : AuthViewModel
){
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val db = DatabaseProvider.getDatabase(context)
    var friends by remember { mutableStateOf<List<UserDB>>(
        listOf()
    ) }
    var displayedFriends by remember { mutableStateOf<List<UserDB>>(
        listOf()
    ) }
    var selected by remember { mutableStateOf<Set<UserDB>>(setOf()) }
    val addSelected : (UserDB) -> Unit = {
        selected = selected + it
    }
    val deleteSelected : (UserDB) -> Unit = {
        selected = selected.filter { user ->
            user != it
        }.toSet()
    }
    var groupName by remember { mutableStateOf("") }
    val fireDB = FirebaseFirestore.getInstance()


    val onSubmit: ( ) -> Unit = {
        val selectedUsers = selected.toList()
        if (selectedUsers.isEmpty() || groupName.isEmpty()){
            Toast.makeText(context, "Please add a group name and make sure you select at least one user", Toast.LENGTH_SHORT).show()
        }
        else {
            val name = groupName
            groupName = ""
            selected = setOf()

            coroutineScope.launch {
                val newSessionRef = fireDB.collection("sessions").document()

                val inviter = authViewModel.currentAppUser
                val usersMap = selectedUsers.associate { it.userName to mutableListOf<String>() }
                    .toMutableMap()
                    .apply { put(inviter, mutableListOf()) }

                val newSession = FirebaseSessions(
                    sessionID = newSessionRef.id,
                    users = usersMap,
                    movies = emptyMap(),
                    numUsers = selectedUsers.size + 1,
                    finalMovie = "",
                    sessionName = name
                )

                fireDB.runBatch { batch ->
                    val currentUserRef = fireDB.collection("users").document(inviter)

                    batch.update(currentUserRef, "Sessions", FieldValue.arrayUnion(newSessionRef.id))
                }

                val data = hashMapOf(
                    "sessionID" to newSession.sessionID,
                    "users" to newSession.users,
                    "movies" to newSession.movies,
                    "numUsers" to newSession.numUsers,
                    "finalMovie" to newSession.finalMovie,
                    "sessionName" to newSession.sessionName
                )

                newSessionRef.set(newSession)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Group created!", Toast.LENGTH_SHORT).show()
                        Log.d("Firestore", "Successfully added to Sessions")
                        selectedUsers.forEach { user ->
                            fireDB.collection("sessionsRequests")
                                .document(user.userName)
                                .update("Requests", FieldValue.arrayUnion(newSessionRef.id))
                                .addOnSuccessListener {
                                    Log.d("Firestore", "Successfully added to Requesters")
                                }
                                .addOnFailureListener { e ->
                                    if (e is FirebaseFirestoreException && e.code == FirebaseFirestoreException.Code.NOT_FOUND) {
                                        // Document doesn't exist, create it
                                        val newDocData =
                                            hashMapOf("Requests" to listOf(newSessionRef.id))
                                        fireDB.collection("sessionsRequests")
                                            .document(user.userName)
                                            .set(newDocData, SetOptions.merge())
                                            .addOnSuccessListener {
                                                Log.d(
                                                    "Firestore",
                                                    "Document created and user added"
                                                )
                                            }
                                            .addOnFailureListener { err ->
                                                Log.e("Firestore", "Error creating document", err)
                                            }
                                    } else {
                                        Log.e("Firestore", "Error updating Requesters", e)
                                    }
                                }
                        }

                        val groupDB = GroupDB(
                            groupID = data["sessionID"] as String,
                            users = data["users"] as Map<String, List<String>>,
                            movies = data["movies"] as Map<String, Int>,
                            pending = false, // Or set the appropriate value
                            numUsers = data["numUsers"] as Int,
                            finalMovie = data["finalMovie"] as String,
                            sessionName = data["sessionName"] as String
                        )

                        coroutineScope.launch() {
                            db.movieDao().insertGroupNotification(groupDB)
                        }

                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Error updating Requesters", e)
                    }


            }
        }
    }


    LaunchedEffect(Unit) {
        friends = db.movieDao().getFriends()
        displayedFriends = friends
    }
    Box {
        Column {
            SearchHeader(
                title = "Add Group",
                rightButtonHandler = {
                    changeAddInProgress()
                },
                searchHandler = {
                    if (it != "" )
                        coroutineScope.launch {
                            displayedFriends = db.movieDao().prefixSearch(it)
                        }
                    else displayedFriends = friends
                },
                searchLabel = "Find Group to Add",
                rightButtonIcon = Icons.Filled.Close
            )
            OutlinedTextField(
                value = groupName,
                onValueChange = {
                    groupName = it
                },
                label = {
                    Text(text = "Group Name")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
            )
            UserToasts(selected, onClick = {
                {
                    deleteSelected(it)
                }
            })
            UserSearchResultsList(displayedFriends, onAddFriend = {
                addSelected(it)
            }, showIcon = true)
        }

        Card(
            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 40.dp, bottom = 40.dp),
            shape = CircleShape,
            elevation = CardDefaults.elevatedCardElevation(10.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
        ) {
            IconButton(onClick = {
                onSubmit()
            }, modifier = Modifier.size(56.dp)) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Add Group",
                    tint = Color.Black,
                    modifier = Modifier
                )
            }
        }
    }
}



@Composable
fun GroupsPage(modifier : Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){
    var addInProgress by remember { mutableStateOf(false) }
    val changeAddInProgress: () -> Unit = { addInProgress = !addInProgress }

    if (!addInProgress){
        Groups(changeAddInProgress = changeAddInProgress, navController = navController)
    }
    else{
        AddGroup(changeAddInProgress = changeAddInProgress, authViewModel)
    }
}


@Composable
fun GroupPreview(
    groupName: String,
    memberProfilePictures: List<Int>,
    onStartSessionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card (
        modifier = modifier,
        shape = RoundedCornerShape(30.dp),
        elevation = CardDefaults.elevatedCardElevation(10.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = groupName,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = onStartSessionClick,
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Start Session",
                        modifier = Modifier.size(35.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Session")
                }
            }
            Spacer(Modifier.height(5.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                memberProfilePictures.forEach { profilePicRes ->
                    Image(
                        painter = painterResource(id = getProfilePicture(profilePicRes)),
                        contentDescription = "Group member profile picture",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                        , contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun GroupScreen() {
    val sampleMembers = listOf(
        R.drawable.joker,
        R.drawable.moviepug,
        R.drawable.wick
    )

    GroupPreview(
        groupName = "The Comedy Crew",
        memberProfilePictures = sampleMembers,
        onStartSessionClick = {
            // Handle start session logic here
        }
    )
}

//
//@Preview
//@Composable
//fun GroupsPagePreview(){
//    GroupsPage()
//}


@Composable
fun UserToast(
    user: UserDB,
    onClick: (UserDB) -> () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 15.dp),
        ) {
            Text(
                text = user.userName,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.width(2.dp))
            IconButton(
                onClick = onClick(user),
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Add Movie",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
        }
    }
}
