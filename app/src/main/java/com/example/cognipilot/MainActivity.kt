package com.example.cognipilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.ui.theme.MyApplicationTheme
import com.example.cognipilot.navigation.AppNavigation
import com.example.cognipilot.ui.components.PersistentOrb

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                Box(modifier = Modifier.fillMaxSize()) {
                    // Main app navigation
                    AppNavigation(navController = navController)
                    
                    // Persistent Orb overlay
                    PersistentOrb(
                        navController = navController,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}
