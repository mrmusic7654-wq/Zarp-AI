package com.example.cognipilot.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    var step by remember { mutableIntStateOf(0) }

    // Auto advancing scenes
    LaunchedEffect(step) {
        if (step == 0) {
            delay(3000)
            step = 1
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable {
                if (step < 3) step++ else onFinish()
            },
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = step,
            transitionSpec = {
                fadeIn(animationSpec = tween(1500)) togetherWith fadeOut(animationSpec = tween(1500))
            },
            label = "onboarding_scenes"
        ) { targetStep ->
            when (targetStep) {
                0 -> {
                    Text("Something is waking up...", color = TextLight, fontSize = 20.sp, fontWeight = FontWeight.Light)
                }
                1 -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Teach me your voice.", color = TextLight, fontSize = 24.sp, fontWeight = FontWeight.Light)
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("(Tap to simulate speaking)", color = TextLight.copy(alpha = 0.5f), fontSize = 14.sp)
                    }
                }
                2 -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Teach me your touch.", color = TextLight, fontSize = 24.sp, fontWeight = FontWeight.Light)
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("(Tap to simulate three-finger gesture)", color = TextLight.copy(alpha = 0.5f), fontSize = 14.sp)
                    }
                }
                3 -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Zarp Ai is yours.", color = SecondaryAmber, fontSize = 28.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("(Tap to enter)", color = TextLight.copy(alpha = 0.5f), fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
