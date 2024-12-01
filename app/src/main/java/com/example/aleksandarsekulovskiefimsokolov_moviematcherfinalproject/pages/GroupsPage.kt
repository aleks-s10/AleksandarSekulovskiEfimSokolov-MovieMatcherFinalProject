package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages

import android.provider.ContactsContract.Groups
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.AuthViewModel

@Composable
fun GroupsContent(){

}

@Composable
fun Groups(changeAddInProgress: () -> Unit, navController: NavController){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            SearchHeader(
                title = "Groups",
                rightButtonHandler =  {
                    changeAddInProgress()
                },
                searchHandler = {},
                searchLabel = "Search Groups",
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



//
//@Preview
//@Composable
//fun GroupsPagePreview(){
//    GroupsPage()
//}