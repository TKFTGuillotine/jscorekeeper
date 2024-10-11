package com.guillotine.jscorekeeper.composable.game

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.guillotine.jscorekeeper.R

@Composable
fun NextRoundDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirmation) {
                Text(text = stringResource(R.string.confirm_continue))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        icon = {
            Icon(
                Icons.Default.Warning,
                contentDescription = stringResource(R.string.warning_icon_description)
            )
        },
        title = {
            Text(text = stringResource(R.string.are_you_sure))
        },
        text = {
            Text(text = stringResource(R.string.next_round_dialog_content))
        }

    )
}

@Preview
@Composable
fun NextRoundDialogPreview() {
    NextRoundDialog(onDismissRequest = {}, onConfirmation = {})
}