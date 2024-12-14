package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages

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
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.launch


@Composable
fun GroupsContent(){

}

@Composable
fun Groups(changeAddInProgress: () -> Unit, navController: NavController){
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
            OutlinedButton(
                onClick = {
                    navController.navigate("swiping")
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 10.dp)
            ) {
                Text("Swiping Screen")
            }
        }
        FooterNavigation(
            modifier = Modifier.align(Alignment.BottomCenter),
            navController = navController
        )
    }
}


@Composable
fun AddGroup(
    changeAddInProgress: () -> Unit
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


    val onSubmit: ( ) -> Unit = {
        val selectedUsers = selected.toList()
//        Submit to Firebase
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
        AddGroup(changeAddInProgress = changeAddInProgress)
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
                        painter = painterResource(id = profilePicRes),
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

data class GroupInfo(
    val groupName: String,
    val memberProfilePictures: List<Int>
)

@Composable
fun GroupsList(
    groups: List<GroupInfo>,
    onStartSessionClick: (GroupInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(groups) { group ->
            GroupPreview(
                groupName = group.groupName,
                memberProfilePictures = group.memberProfilePictures,
                onStartSessionClick = { onStartSessionClick(group) }
            )
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

@Preview
@Composable
fun GroupsScreen() {
    val sampleGroups = listOf(
        GroupInfo(
            groupName = "The Comedy Crew",
            memberProfilePictures = listOf(
                R.drawable.joker,
                R.drawable.moviepug,
                R.drawable.wick
            )
        ),
        GroupInfo(
            groupName = "Action Aces",
            memberProfilePictures = listOf(
                R.drawable.moana,
                R.drawable.panda,
            )
        ),
        GroupInfo(
            groupName = "Horror Horde",
            memberProfilePictures = listOf(
                R.drawable.deadpool,
                R.drawable.minecraft,
                R.drawable.mazerunner,
                R.drawable.thumbsup
            )
        )
    )

    GroupsList(
        groups = sampleGroups,
        onStartSessionClick = { clickedGroup ->
            // Handle start session logic for clickedGroup
            // E.g., navigate to a session screen or show a toast
        }
    )
}

//
//@Preview
//@Composable
//fun GroupsPagePreview(){
//    GroupsPage()
//}


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
fun UserToast(
    user: UserDB,
    onClick: (UserDB) -> () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick(user) },
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
