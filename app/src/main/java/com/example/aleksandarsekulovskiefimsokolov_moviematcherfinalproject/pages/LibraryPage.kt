package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
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
            Text("IDK")
            SubcomposeAsyncImage(
                model = "https://image.tmdb.org/t/p/w500/yh64qw9mgXBvlaWDi7Q9tpUBAvH.jpg?api_key=a4a43632b097a28262e8e7673da3866e",
                contentDescription = "Image",
                modifier = Modifier.fillMaxWidth(),
                loading = { CircularProgressIndicator() },
//                error = { Text("Could not load this BS") }
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

@Preview
@Composable
fun ImagePreview(){
    val base_url = "https://image.tmdb.org/t/p/w500"
    SubcomposeAsyncImage(
        model = "$base_url/yh64qw9mgXBvlaWDi7Q9tpUBAvH.jpg",
        contentDescription = "Image",
        modifier = Modifier.fillMaxWidth(),
        loading = { CircularProgressIndicator() },
    )
}