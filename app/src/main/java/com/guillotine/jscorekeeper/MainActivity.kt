package com.guillotine.jscorekeeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.guillotine.jscorekeeper.composable.MenuScreenComposable
import com.guillotine.jscorekeeper.ui.theme.JScorekeeperTheme
import kotlinx.serialization.Serializable

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
                        MenuScreenComposable(navController, applicationContext)
                    }
                    composable<GameScreen> {
                        Text("Game Screen!")
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