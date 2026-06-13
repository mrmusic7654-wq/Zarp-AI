package com.example.cognipilot.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun SettingsScreen(onNavigateBack: () -> Unit) {
    var tokenBudget by remember { mutableFloatStateOf(0.7f) }
    var allowProactive by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpace)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextLight)
            }
            Text("Control Matrix", style = Typography.headlineMedium, color = TextLight)
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        // Control Panel Section
        Text("Safety Parameters", color = SecondaryAmber, fontSize = 18.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(GlassSurface, RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Proactive Suggestions", color = TextLight)
                    Switch(
                        checked = allowProactive,
                        onCheckedChange = { allowProactive = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = PrimaryElectricIndigo, checkedTrackColor = LightGlass)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text("API Token Flow Limit", color = TextLight)
                Slider(
                    value = tokenBudget,
                    onValueChange = { tokenBudget = it },
                    colors = SliderDefaults.colors(thumbColor = PrimaryElectricIndigo, activeTrackColor = SecondaryAmber)
                )
            }
        }
    }
}
