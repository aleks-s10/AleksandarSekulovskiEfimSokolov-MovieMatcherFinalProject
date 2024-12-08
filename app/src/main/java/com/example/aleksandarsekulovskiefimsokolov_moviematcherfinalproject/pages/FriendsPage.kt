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
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.SearchManager
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

@Composable
fun FriendsContent(){

}

@Composable
fun Friends(changeAddInProgress: () -> Unit, navController: NavController){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            SearchHeader(
                title = "Friends",
                rightButtonHandler =  {
                    changeAddInProgress()
                },
                searchHandler = {},
                searchLabel = "Search Friends",
                rightButtonIcon = Icons.Filled.Add,
            )
        }
        FooterNavigation(
            modifier = Modifier.align(Alignment.BottomCenter),
            navController = navController
        )
    }
}


@Composable
fun AddFriend(
    changeAddInProgress: () -> Unit
){
    SearchHeader(
        title = "Add Friend",
        rightButtonHandler =  {
            changeAddInProgress()
        },
        searchHandler = {},
        searchLabel = "Find Friend to Add",
        rightButtonIcon = Icons.Filled.Close
    )
}



@Composable
fun FriendsPage(modifier : Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){
    var addInProgress by remember { mutableStateOf(false) }
    val changeAddInProgress: () -> Unit = { addInProgress = !addInProgress }
    var searchManager = SearchManager()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            searchManager.searchUsers("efi", "algoliaUsers")
        }
    }

    if (!addInProgress){
        Friends(changeAddInProgress = changeAddInProgress, navController = navController)
    }
    else{
        AddFriend(changeAddInProgress = changeAddInProgress)
    }
}




//@Preview
//@Composable
//fun FriendsPagePreview(){
//    FriendsPage()
//}