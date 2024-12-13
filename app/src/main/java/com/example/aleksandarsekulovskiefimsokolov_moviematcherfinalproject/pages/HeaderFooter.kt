package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(
    searchQuery: String,
    changeSearchQuery: (String) -> Unit,
    searchHandler: (String) -> Unit,
    searchLabel: String
){
    val searchColor = Color.LightGray.copy(alpha = 0.3f)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = searchColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { searchHandler(searchQuery) }
            ){
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(33.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = searchQuery,
                onValueChange = { changeSearchQuery(it) },
                placeholder = { Text(text = searchLabel) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = searchColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
//                        cursorColor = MaterialTheme.colors.primary
                )
            )
        }
    }
}

@Composable
fun SearchHeader(
    title: String,
    rightButtonIcon: ImageVector,
    rightButtonHandler: () -> Unit,
    searchHandler: (String) -> Unit,
    searchLabel: String,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = modifier
        .fillMaxWidth()
        .padding(16.dp)) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            IconButton(onClick = rightButtonHandler) {
                Icon(
                    imageVector = rightButtonIcon,
                    contentDescription = "Add Movie",
                    modifier = Modifier.size(35.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Search(
            searchHandler = searchHandler,
            searchQuery = searchQuery,
            searchLabel = searchLabel,
            changeSearchQuery = {
                searchQuery = it
            })

        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun TrendingHeader(
    title: String,
    modifier: Modifier = Modifier,
    firstPage: Boolean,
    leftButtonHandler: () -> Unit,
    rightButtonHandler: () -> Unit
){
    Column (modifier = Modifier.padding(start = 16.dp, end = 16.dp)){
        Spacer(modifier = Modifier.height(45.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            if (firstPage ){
                IconButton(onClick = rightButtonHandler) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "Add Movie",
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
            else{
                Row{
                    IconButton(onClick = leftButtonHandler) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Add Movie",
                            modifier = Modifier.size(35.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    IconButton(onClick = rightButtonHandler) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "Add Movie",
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}


@Composable
fun FooterNavigation(modifier: Modifier = Modifier, navController: NavController) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = MaterialTheme.colorScheme.primary),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = { navController.navigate("Trending")},
//                modifier = Modifier.padding(bottom = 5.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = "Trending",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        IconButton(
            onClick = { navController.navigate("friends") }
        ) {
            Icon(
                imageVector = Icons.Filled.Face,
                contentDescription = "friends",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        IconButton(
            onClick = { navController.navigate("groups") }
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "groups",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        IconButton(
            onClick = { navController.navigate("profile") }
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

//
//@Preview(showBackground = true)
//@Composable
//fun FooterNavigationPreview() {
//    MaterialTheme {
//        FooterNavigation()
//    }
//}
//
