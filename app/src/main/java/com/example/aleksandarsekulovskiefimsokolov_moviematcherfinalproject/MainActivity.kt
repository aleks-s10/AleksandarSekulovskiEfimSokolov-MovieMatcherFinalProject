package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import java.io.IOException
import android.util.Log
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.DebugLogger
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.MovieAPI
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.MovieDB
import com.google.firebase.firestore.firestore
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Interceptor

class MainActivity : ComponentActivity(), ImageLoaderFactory{
    private lateinit var auth: FirebaseAuth
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        analytics = Firebase.analytics
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = Firebase.firestore
        val authViewModel: AuthViewModel by viewModels()
        setContent {
            AleksandarSekulovskiEfimSokolovMovieMatcherFinalProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyAppNavigation(
                        modifier = Modifier.padding(innerPadding),
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader(context = this).newBuilder()
            .callFactory{
                Call.Factory {
                    OkHttpClient().newCall(
                        it.newBuilder().url(
                            it.url.toString()
                            + "?api_key=a4a43632b097a28262e8e7673da3866e").build()
                    )
                }
            }
            .logger(DebugLogger())
            .build()
    }


}