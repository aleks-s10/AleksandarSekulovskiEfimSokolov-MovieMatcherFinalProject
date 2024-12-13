package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.R

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
                modifier = Modifier.align(Alignment.End).padding(end = 10.dp)
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
    SearchHeader(
        title = "Add Group",
        rightButtonHandler =  {
            changeAddInProgress()
        },
        searchHandler = {},
        searchLabel = "Find Group to Add",
        rightButtonIcon = Icons.Filled.Close
    )
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