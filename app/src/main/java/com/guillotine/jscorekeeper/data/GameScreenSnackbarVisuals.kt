package com.guillotine.jscorekeeper.data

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

class GameScreenSnackbarVisuals(override val message: String) : SnackbarVisuals {
    override val duration = SnackbarDuration.Short
    override val withDismissAction = false
    override val actionLabel = null
}