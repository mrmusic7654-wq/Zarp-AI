package com.example.cognipilot.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun HistoryScreen(onNavigateBack: () -> Unit, onNavigateToTask: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(DeepSpace)) {
        // Vertical River Stream Mock
        Box(modifier = Modifier.fillMaxHeight().width(4.dp).background(PrimaryElectricIndigo.copy(alpha = 0.5f)).align(Alignment.Center))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 100.dp, bottom = 100.dp)
        ) {
            items(10) { index ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalArrangement = if (index % 2 == 0) Arrangement.Start else Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 40.dp)
                            .width(200.dp)
                            .background(GlassSurface, RoundedCornerShape(16.dp))
                            .blur(4.dp)
                            .clickable { onNavigateToTask() }
                            .padding(16.dp)
                    ) {
                        Column {
                            Text("Mock Task $index", color = TextLight, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("10:${index}0 AM", color = TextLight.copy(alpha = 0.7f), fontSize = 12.sp)
                        }
                    }
                }
            }
        }
        
        Row(
            modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.safeDrawing).padding(16.dp).align(Alignment.TopStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextLight)
            }
            Text("Time Stream", style = Typography.headlineMedium, color = TextLight)
        }
    }
}
