package com.guillotine.jscorekeeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.guillotine.jscorekeeper.composable.game.GameScreenComposable
import com.guillotine.jscorekeeper.composable.menu.MenuScreenComposable
import com.guillotine.jscorekeeper.ui.theme.JScorekeeperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JScorekeeperTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = MenuScreen
                ) {
                    composable<MenuScreen> {
                        MenuScreenComposable(navController)
                    }
                    composable<GameScreen> {
                        GameScreenComposable(navController, it.toRoute<GameScreen>())
                    }
                    composable<PastGamesListScreen> {
                        Text("History Screen!")
                    }
                }
            }

        }
    }
}

// Basic architecture:
// Screens for menu, game, stats, past games.
// Game should be self-contained, "back" should save state and return to menu.
// Game should remove itself from the back stack when finished.
// Game should be flexible, capable of running the three different English-language formats.