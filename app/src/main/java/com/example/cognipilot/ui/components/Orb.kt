package com.example.cognipilot.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.cognipilot.navigation.Routes
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

import com.example.cognipilot.utils.HapticManager
import kotlinx.coroutines.delay

enum class OrbState {
    IDLE, THINKING
}

@Composable
fun PersistentOrb(navController: NavHostController, modifier: Modifier = Modifier) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var orbState by remember { mutableStateOf(OrbState.IDLE) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val hapticManager = remember { HapticManager(context) }

    val infiniteTransition = rememberInfiniteTransition(label = "orb_rotation")
    
    // Rotate faster when thinking
    val rotationDuration = if (orbState == OrbState.THINKING) 2000 else 8000
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(rotationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    LaunchedEffect(orbState) {
        if (orbState == OrbState.THINKING) {
            hapticManager.pop()
            while (true) {
                delay(1000)
                hapticManager.breathingPulse(0.6f)
                delay(1000)
            }
        } else {
            hapticManager.click()
            while (true) {
                delay((2000..5000).random().toLong())
                hapticManager.tick() // Subtle click simulating a particle collision
            }
        }
    }

    // Black hole scale animation
    val blackHoleScale by animateFloatAsState(
        targetValue = if (orbState == OrbState.THINKING) 0.9f else 0.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "black_hole_scale"
    )

    // Glow intensity
    val glowIntensity by animateFloatAsState(
        targetValue = if (orbState == OrbState.THINKING) 0.5f else 0.2f,
        animationSpec = tween(500),
        label = "glow_intensity"
    )

    // Time metrics
    val timeMetrics = LocalTimeMetrics.current
    val dynamicBloomMultiplier = timeMetrics.bloomIntensity
    val dynamicGlassAlpha = timeMetrics.glassAlpha

    // A premium glass orb that is persistent
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .size(192.dp) // 48 * 4 = 192dp
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                orbState = if (orbState == OrbState.IDLE) OrbState.THINKING else OrbState.IDLE
            }
    ) {
        // Outer glow 1
        Box(modifier = Modifier.fillMaxSize().blur(80.dp).background(PrimaryIndigo500.copy(alpha = glowIntensity * dynamicBloomMultiplier), CircleShape))
        // Outer glow 2
        Box(modifier = Modifier.fillMaxSize(0.9f).blur(40.dp).background(PrimaryIndigo400.copy(alpha = glowIntensity * 0.5f * dynamicBloomMultiplier), CircleShape))
        
        // Main Sphere
        Box(
            modifier = Modifier
                .fillMaxSize(0.8f)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            if (orbState == OrbState.THINKING) SecondaryAmber.copy(alpha = 0.5f) else PrimaryIndigo500, 
                            Color(0xFF0A0A1A), 
                            Color.Black
                        )
                    ),
                    shape = CircleShape
                )
                .border(
                    width = if (orbState == OrbState.THINKING) 2.dp else 1.dp,
                    color = if (orbState == OrbState.THINKING) SecondaryAmber.copy(alpha=0.4f) else GlassBorder.copy(alpha=dynamicGlassAlpha*2),
                    shape = CircleShape
                )
                .shadow(elevation = if (orbState == OrbState.THINKING) 24.dp else 12.dp, shape = CircleShape, spotColor = if (orbState == OrbState.THINKING) SecondaryAmber else PrimaryIndigo500),
            contentAlignment = Alignment.Center
        ) {
            // Internal vortex
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(rotation)
                    .background(
                        brush = Brush.sweepGradient(
                            colors = if (orbState == OrbState.THINKING) {
                                listOf(SecondaryAmber, Color.Transparent, PrimaryElectricIndigo, Color.Transparent)
                            } else {
                                listOf(PrimaryElectricIndigo, Color.Transparent, SecondaryAmber, Color.Transparent)
                            }
                        ),
                        shape = CircleShape
                    )
            )
            // Inner frosted glass
            Box(
                modifier = Modifier
                    .fillMaxSize(0.85f)
                    .background(GlassSurface.copy(alpha=dynamicGlassAlpha), CircleShape)
                    .border(1.dp, GlassBorder.copy(alpha=dynamicGlassAlpha), CircleShape)
                    .blur(4.dp)
            )
            
            // Thinking state - Black hole center
            if (blackHoleScale > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(blackHoleScale * 0.6f)
                        .background(Color.Black, CircleShape)
                        .border(1.dp, SecondaryAmber.copy(alpha=0.5f), CircleShape)
                        .shadow(16.dp, CircleShape, spotColor = Color.Black)
                ) {
                    // Accretion disk edge glow
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(4.dp, Brush.sweepGradient(listOf(SecondaryAmber, PrimaryElectricIndigo, SecondaryAmber)), CircleShape)
                            .blur(8.dp)
                            .rotate(-rotation * 2)
                    )
                }
            }

            // Core light
            if (orbState == OrbState.IDLE) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color.White, CircleShape)
                        .blur(4.dp)
                )
            }
        }
    }
}
