package com.example.cognipilot.ui.screens

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.border
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import java.util.Calendar
import kotlin.random.Random

@Composable
fun rememberSensorParallaxOffset(): Offset {
    val context = LocalContext.current
    var offset by remember { mutableStateOf(Offset.Zero) }

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) ?: sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                // Smooth mapping from gravity/accelerometer to screen offset pixels
                // event.values[0] is X axis gravity (-9.8 to 9.8)
                // event.values[1] is Y axis gravity
                val x = event.values[0] * 12f
                val y = event.values[1] * 12f
                offset = Offset(x, y)
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        
        sensor?.let {
            sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_UI)
        }
        
        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }
    
    // Smooth the offset to avoid jitter
    val animatedX by animateFloatAsState(targetValue = offset.x, animationSpec = tween(100), label = "px")
    val animatedY by animateFloatAsState(targetValue = offset.y, animationSpec = tween(100), label = "py")
    
    return Offset(animatedX, animatedY)
}

@Composable
fun DashboardScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToTask: () -> Unit,
    onOverlayRequested: () -> Unit
) {
    // Spatial Parallax Mock
    val infiniteTransition = rememberInfiniteTransition(label = "parallax_float")
    val floatingOffset by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "parallax_anim"
    )

    val hardwareParallax = rememberSensorParallaxOffset()
    
    val timeMetrics = LocalTimeMetrics.current
    
    // Time of Day based colors
    val (nebulaColor1, nebulaColor2) = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 5..8 -> DawnPink to SecondaryAmber // Dawn
            in 9..16 -> DayBlue to PrimaryElectricIndigo // Day
            in 17..19 -> DawnPink to NightPurple // Dusk
            else -> NightPurple to PrimaryIndigo500 // Night
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpace)
    ) {
        // Deep Space Particles & Nebula (Editorial Aesthetic)
        val particles = remember { List(150) { Offset(Random.nextFloat(), Random.nextFloat()) } }
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            
            // Base ambient layer
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Black, DeepSpace, Color(0xFF030308))
                ),
                size = size
            )
            
            // Nebula layers with hardware parallax
            val radius = maxOf(w, h) * 0.9f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(nebulaColor1.copy(alpha=0.3f), Color.Transparent),
                    center = Offset(w * 0.3f + hardwareParallax.x * 2, h * 0.3f + floatingOffset + hardwareParallax.y * 2),
                    radius = radius
                ),
                radius = radius,
                center = Offset(w * 0.3f + hardwareParallax.x * 2, h * 0.3f + floatingOffset + hardwareParallax.y * 2),
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(nebulaColor2.copy(alpha=0.25f), Color.Transparent),
                    center = Offset(w * 0.8f - hardwareParallax.x * 1.5f, h * 0.7f - floatingOffset + hardwareParallax.y * 1.5f),
                    radius = radius * 0.8f
                ),
                radius = radius * 0.8f,
                center = Offset(w * 0.8f - hardwareParallax.x * 1.5f, h * 0.7f - floatingOffset + hardwareParallax.y * 1.5f),
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.Black.copy(alpha=0.5f), Color.Transparent),
                    center = Offset(w * 0.5f, h * 0.5f),
                    radius = radius * 0.7f
                ),
                radius = radius * 0.7f,
                center = Offset(w * 0.5f, h * 0.5f)
            )

            // Grid pattern
            val gridSize = 40f
            for (x in 0..(w / gridSize).toInt()) {
                for (y in 0..(h / gridSize).toInt()) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.05f),
                        radius = 1f,
                        center = Offset(x * gridSize + hardwareParallax.x * 0.5f, y * gridSize + hardwareParallax.y * 0.5f)
                    )
                }
            }

            // Particles with depth/parallax
            particles.forEachIndexed { index, p ->
                val depth = (index % 3) + 1f // 1, 2, or 3
                val speedMultiplier = depth * 0.5f
                
                val px = (p.x * w + floatingOffset * speedMultiplier * 2 + hardwareParallax.x * speedMultiplier).mod(w)
                val py = (p.y * h + (floatingOffset * speedMultiplier) + hardwareParallax.y * speedMultiplier).mod(h)
                
                drawCircle(
                    color = Color.White.copy(alpha = (0.2f / depth) + 0.1f),
                    radius = (2.5f / depth) + (Random.nextFloat() * 1f),
                    center = Offset(px, py)
                )
            }
        }

        // Status Bar Mock Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "ZARP AI // SYSTEM.LIVE",
                style = Typography.labelSmall,
                color = TextIndigo300,
            )
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.size(6.dp).background(EmeraldPulse, CircleShape).shadow(6.dp, CircleShape, spotColor = EmeraldPulse))
                Text("Sync 98.4%", style = Typography.labelMedium, color = TextLight.copy(alpha = 0.6f), fontWeight = FontWeight.Medium)
            }
        }

        // Center content area
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            
            // Memory Constellation (Orbs)
            val memoryPositions = listOf(
                Offset(-120f, -240f),
                Offset(160f, -180f),
                Offset(-220f, 140f),
                Offset(140f, 220f),
            )
            memoryPositions.forEachIndexed { index, offset ->
                Box(
                    modifier = Modifier
                        .offset(
                            x = (offset.x + hardwareParallax.x * ((index % 2) + 1)).dp, 
                            y = (offset.y + hardwareParallax.y * ((index % 2) + 1) + floatingOffset * (if(index%2==0) 1f else -1f)).dp
                        )
                        .size(44.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(LightGlass.copy(alpha=0.6f), Color.Transparent)
                            ),
                            shape = CircleShape
                        )
                        .blur(2.dp)
                        .clickable { onNavigateToTask() }
                ) {
                    Box(modifier = Modifier.fillMaxSize(0.3f).align(Alignment.Center).background(Color.White.copy(alpha=0.8f), CircleShape).blur(1.dp))
                }
            }

            // Task Information & Active Chips (Editorial Typography)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 180.dp) // Pushed down to leave space for Orb
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        "Optimizing ",
                        style = Typography.headlineMedium,
                        color = Color.White
                    )
                    Text(
                        "Workflow",
                        style = Typography.headlineMedium.copy(fontStyle = FontStyle.Italic),
                        color = PrimaryIndigo400
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Synthesizing cognitive patterns for your 14:00 briefing.\nNeural pathways stabilized.",
                    style = Typography.bodyLarge,
                    color = TextIndigo200Op60,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(300.dp)
                )

                Spacer(modifier = Modifier.height(36.dp))
                
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(
                        modifier = Modifier
                            .background(GlassSurface.copy(alpha=0.2f), CircleShape)
                            .border(1.dp, GlassBorder.copy(alpha=0.5f), CircleShape)
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text("REASONING", style = Typography.labelSmall, color = TextIndigo300)
                    }
                    Box(
                        modifier = Modifier
                            .background(PrimaryIndigo500.copy(alpha=0.4f), CircleShape)
                            .border(1.dp, PrimaryIndigo400.copy(alpha=0.6f), CircleShape)
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text("SYNTHESIS", style = Typography.labelSmall, color = Color.White)
                    }
                }
            }
        }

        // Foreground Glass Dock
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(bottom = 32.dp, start = 24.dp, end = 24.dp)
                .fillMaxWidth()
                .height(84.dp)
                .background(GlassSurface.copy(alpha=timeMetrics.surfaceAlpha), shape = CircleShape)
                .shadow(elevation = 32.dp, shape = CircleShape, spotColor = Color.Black)
                .border(1.dp, GlassBorder.copy(alpha=timeMetrics.glassAlpha), CircleShape)
                .blur(16.dp) // background blur overlay approximation
        )
        
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(bottom = 32.dp, start = 24.dp, end = 24.dp)
                .fillMaxWidth()
                .height(84.dp)
                .background(GlassSurface.copy(alpha=timeMetrics.surfaceAlpha * 1.5f), CircleShape) // Slightly tint the foreground over the blur
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        brush = Brush.linearGradient(listOf(PrimaryIndigo400, PrimaryIndigo500)), 
                        shape = CircleShape
                    )
                    .shadow(16.dp, CircleShape, spotColor = PrimaryIndigo500)
                    .clickable { onOverlayRequested() },
                contentAlignment = Alignment.Center
            ) {
                // Command Mic Icon representation
                Icon(Icons.Default.Search, contentDescription = "Command", tint = Color.White, modifier = Modifier.size(28.dp))
            }
            
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.weight(1f)
            ) {
                IconButton(onClick = onNavigateToHistory) {
                    Icon(Icons.AutoMirrored.Filled.List, contentDescription = "History", tint = TextIndigo300.copy(alpha = 0.8f))
                }
                IconButton(onClick = { /* Explore action */ }) {
                    Icon(Icons.Default.Settings, contentDescription = "Explore", tint = TextIndigo300.copy(alpha = 0.8f))
                }
                IconButton(onClick = onNavigateToSettings) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = TextIndigo300.copy(alpha = 0.8f))
                }
            }

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(GlassSurface, CircleShape)
                    .border(1.dp, GlassBorder.copy(alpha=0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.size(28.dp).border(2.dp, PrimaryIndigo400.copy(alpha=0.8f), CircleShape))
            }
        }
    }
}
