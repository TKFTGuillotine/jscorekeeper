package com.guillotine.jscorekeeper.viewmodels

import androidx.lifecycle.ViewModel
import com.guillotine.jscorekeeper.data.SavedGame

class MenuScreenViewModel(): ViewModel() {
    fun isSavedGame(savedGame: SavedGame?): Boolean {
        // Arbitrary, but it would be impossible to have a board with no columns, so this being a
        // default value in the serializer, it's an easy way to tell if there's a game or not.
        return savedGame?.gameData?.columns != 0
    }
}