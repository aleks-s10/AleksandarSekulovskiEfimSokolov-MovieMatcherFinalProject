package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages

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
fun LibraryContent(){

}

@Composable
fun Library(changeAddInProgress: () -> Unit, navController: NavController){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            SearchHeader(
                title = "Library",
                rightButtonHandler =  {
                    changeAddInProgress()
                },
                searchHandler = {},
                searchLabel = "Search Library",
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
fun AddMovie(
    changeAddInProgress: () -> Unit
){
    SearchHeader(
        title = "Add Movie",
        rightButtonHandler =  {
            changeAddInProgress()
        },
        searchHandler = {},
        searchLabel = "Find Movie to Add",
        rightButtonIcon = Icons.Filled.Close
    )
}



@Composable
fun LibraryPage(modifier : Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){
    var addInProgress by remember { mutableStateOf(false) }
    val changeAddInProgress: () -> Unit = { addInProgress = !addInProgress }

    if (!addInProgress){
        Library(changeAddInProgress = changeAddInProgress, navController = navController)
    }
    else{
        AddMovie(changeAddInProgress = changeAddInProgress)
    }
}


//
//
//@Preview
//@Composable
//fun LibraryPagePreview(){
//    LibraryPage()
//}