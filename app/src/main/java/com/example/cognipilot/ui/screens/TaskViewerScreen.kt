package com.example.cognipilot.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun TaskViewerScreen(onNavigateBack: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpace)
    ) {
        // Grid background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val step = 100f
            for (x in 0..(size.width / step).toInt()) {
                drawLine(
                    color = GlassSurface,
                    start = androidx.compose.ui.geometry.Offset(x * step, 0f),
                    end = androidx.compose.ui.geometry.Offset(x * step, size.height),
                    strokeWidth = 1f
                )
            }
            for (y in 0..(size.height / step).toInt()) {
                drawLine(
                    color = GlassSurface,
                    start = androidx.compose.ui.geometry.Offset(0f, y * step),
                    end = androidx.compose.ui.geometry.Offset(size.width, y * step),
                    strokeWidth = 1f
                )
            }
        }

        // Neural Pathway map
        Canvas(modifier = Modifier.fillMaxSize()) {
            val path = Path().apply {
                moveTo(200f, size.height / 2f)
                cubicTo(
                    400f, size.height / 2f,
                    400f, size.height / 2f - 300f,
                    600f, size.height / 2f - 300f
                )
                moveTo(200f, size.height / 2f)
                cubicTo(
                    400f, size.height / 2f,
                    400f, size.height / 2f + 200f,
                    600f, size.height / 2f + 200f
                )
            }

            drawPath(
                path = path,
                color = PrimaryElectricIndigo.copy(alpha = 0.5f),
                style = Stroke(
                    width = 8f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
            
            // Nodes
            drawCircle(
                color = Color.White.copy(alpha = pulseAlpha),
                radius = 30f,
                center = androidx.compose.ui.geometry.Offset(200f, size.height/2f)
            )
            drawCircle(
                color = SecondaryAmber,
                radius = 20f,
                center = androidx.compose.ui.geometry.Offset(600f, size.height/2f + 200f)
            )
        }
        
        Text(
            text = "Active Reasoning...",
            style = Typography.headlineMedium,
            color = TextLight,
            modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing).padding(32.dp).align(Alignment.TopStart)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(32.dp)
                .size(64.dp)
                .background(DangerCrimson, CircleShape)
                .blur(4.dp)
                .clickable { onNavigateBack() },
            contentAlignment = Alignment.Center
        ) {
            Text("Back", color = Color.White)
        }
    }
}
