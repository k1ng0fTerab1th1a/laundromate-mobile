package com.example.laundromate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.laundromate.presentation.auth.LoginScreen
import com.example.laundromate.presentation.auth.RegisterScreen
import com.example.laundromate.presentation.laundries.LaundriesScreen
import com.example.laundromate.ui.theme.LaundroMateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaundroMateTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LaundroMateApp()
                }
            }
        }
    }
}

@Composable
fun LaundroMateApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = { navController.navigate("laundries") {
                    popUpTo("login") { inclusive = true }
                }}
            )
        }
        composable("register") {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate("login") }
            )
        }
        composable("laundries") {
            LaundriesScreen()
        }
    }
}