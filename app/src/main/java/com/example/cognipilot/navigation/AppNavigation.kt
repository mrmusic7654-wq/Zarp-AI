package com.example.cognipilot.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cognipilot.ui.screens.DashboardScreen
import com.example.cognipilot.ui.screens.HistoryScreen
import com.example.cognipilot.ui.screens.InputOverlayScreen
import com.example.cognipilot.ui.screens.OnboardingScreen
import com.example.cognipilot.ui.screens.SettingsScreen
import com.example.cognipilot.ui.screens.TaskViewerScreen

object Routes {
    const val ONBOARDING = "onboarding"
    const val DASHBOARD = "dashboard"
    const val HISTORY = "history"
    const val SETTINGS = "settings"
    const val TASK_VIEWER = "task_viewer"
    const val INPUT_OVERLAY = "input_overlay"
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.ONBOARDING
    ) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onNavigateToSettings = { navController.navigate(Routes.SETTINGS) },
                onNavigateToHistory = { navController.navigate(Routes.HISTORY) },
                onNavigateToTask = { navController.navigate(Routes.TASK_VIEWER) },
                onOverlayRequested = { navController.navigate(Routes.INPUT_OVERLAY) }
            )
        }
        composable(Routes.HISTORY) {
            HistoryScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTask = { navController.navigate(Routes.TASK_VIEWER) }
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.TASK_VIEWER) {
            TaskViewerScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.INPUT_OVERLAY) {
            InputOverlayScreen(
                onNavigateBack = { navController.popBackStack() },
                onLaunchTask = { navController.navigate(Routes.TASK_VIEWER) }
            )
        }
    }
}
