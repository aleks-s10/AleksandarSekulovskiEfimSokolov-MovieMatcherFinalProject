package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.data.apiKey
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.Movies
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.ui.theme.AleksandarSekulovskiEfimSokolovMovieMatcherFinalProjectTheme
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.RetrofitInstance
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.io.IOException
import android.util.Log
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        analytics = Firebase.analytics
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel: AuthViewModel by viewModels()
        setContent {
            AleksandarSekulovskiEfimSokolovMovieMatcherFinalProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyAppNavigation(
                        modifier = Modifier.padding(innerPadding),
                        authViewModel = authViewModel
                    )

                    val context = LocalContext.current
                    val scope = rememberCoroutineScope()

                    // Fetch data when the composable is loaded
                    LaunchedEffect(key1 = true) {
                        println("GOT HERE")
                        scope.launch(Dispatchers.IO) {
                            val response = try {
                                println("GOT HERE")
                                val response = RetrofitInstance.api.getPopularMovies()
                                println("RAW RESPONSE: ${response.raw()}")
                            } catch (e: HttpException) {
                                println("GOT HERE EXCEPTION: ${e.message}")
                                // Handle HTTP exception on the main thread
                                launch(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "HTTP error: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                return@launch
                            } catch (e: IOException) {
                                println("GOT HERE EXCEPTION: ${e.message}")

                                // Handle IO exception on the main thread
                                launch(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "IO Exception error: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                return@launch
                            }
                            catch (e: Exception) {
                                println("GOT HERE EXCEPTION: ${e.message}")
                                launch(Dispatchers.Main) {
                                    Toast.makeText(context, "Unexpected error: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                                return@launch
                            }
                            println("GOT HERE2")
//                            if (response.isSuccessful && response.body() != null) {
//                                println("GOT HERE SUCCESS")
//                                val movies = response.body()!!
//                                launch(Dispatchers.Main) {
//                                    // Update UI or show success message
//                                    Toast.makeText(
//                                        context,
//                                        "Get request successful",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                    Log.d("API_RESPONSE", "Movies: $movies")// Debug: Print the response data
//                                }
//                            } else {
//                                println("GOT HERE FAILURE")
//                                launch(Dispatchers.Main) {
//                                    Toast.makeText(
//                                        context,
//                                        "Failed: ${response.message()}",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
//                                Log.d("API_RESPONSE", "FAILED: ${response.message()}")
//                            }
                            println("GOT HERE3")

                        }
                        scope.launch(Dispatchers.IO) {
                            try {
                                val url = URL("https://google.com/")
                                val connection = url.openConnection() as HttpURLConnection
                                connection.requestMethod = "GET"
                                connection.connect()
                                val responseCode = connection.responseCode
                                println("Response Code: $responseCode")
                            } catch (e: Exception) {
                                println("Response Code: FAILED GOOGLE CHECK" + e.message)
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        }
    }
}