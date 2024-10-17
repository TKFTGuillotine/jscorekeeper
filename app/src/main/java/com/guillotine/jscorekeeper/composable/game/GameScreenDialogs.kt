package com.guillotine.jscorekeeper.composable.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.ui.theme.ClueCardTheme
import com.guillotine.jscorekeeper.ui.theme.ClueDialogActiveButton
import com.guillotine.jscorekeeper.ui.theme.ClueDialogInactiveButton

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
            Text(text = stringResource(R.string.move_to_next_round))
        },
        text = {
            Text(text = stringResource(R.string.next_round_dialog_content))
        }

    )
}

@Composable
fun ClueDialog(
    onDismissRequest: () -> Unit,
    value: Int,
    currency: String,
    isRemainingDailyDouble: Boolean,
    onCorrect: (Int) -> Unit,
    onIncorrect: (Int) -> Unit,
    onDailyDouble: (Int) -> Unit,
    onPass: (Int) -> Unit
) {
    ClueCardTheme {
        Dialog(onDismissRequest = onDismissRequest) {
            Card(
                modifier = Modifier
                    // Per M3 spec, the largest possible width.
                    .width(560.dp)
                    .height(IntrinsicSize.Max),
                // Per M3 spec, the correct amount of roundness.
                shape = RoundedCornerShape(28.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        // Per M3 spec, the correct amount of padding.
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max)
                            // Per M3 spec, the correct amount of title padding.
                            .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 16.dp),
                        // Per M3 spec, titles are left aligned if there is no icon. That said, I've
                        // modified enough here that I think that looks ugly as sin, so I'm centering
                        // it in this case.
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${currency}${value}",
                            // This is not specified in M3 spec, so this is basically arbitrary.
                            style = MaterialTheme.typography.headlineLarge,
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max)
                            // Technically, M3 spec suggests the button section have a 24dp padding at
                            // the top, but that usually has content and not title preceding it. So I'm not
                            // sure what the right thing to do here is, but I'm adding 8dp on the top to the
                            // 16dp from the title. The trailing 24dp is to spec, however.
                            .padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 24.dp),
                        // This is not to spec, but that's merely because I think this will be more usable.
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { onCorrect(value) },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                text = stringResource(R.string.correct),
                            )
                        }
                        TextButton(
                            onClick = { onIncorrect(value) },
                            modifier = Modifier.weight(1f),

                        ) {
                            Text(
                                text = stringResource(R.string.incorrect),
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max),
                        // This is not to spec, but that's merely because I think this will be more usable.
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { onPass(value) },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                text = stringResource(R.string.pass),
                            )
                        }
                        TextButton(
                            onClick = { onDailyDouble(value) },
                            modifier = Modifier.weight(1f),
                            enabled = isRemainingDailyDouble,
                        ) {
                            Text(
                                text = stringResource(R.string.daily_double)
                            )
                        }
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun NextRoundDialogPreview() {
    NextRoundDialog(onDismissRequest = {}, onConfirmation = {})
}

@Preview
@Composable
fun ClueDialogPreview() {
    ClueDialog(
        onDismissRequest = {},
        value = 100,
        currency = "$",
        isRemainingDailyDouble = false,
        onCorrect = {},
        onIncorrect = {},
        onDailyDouble = {},
        onPass = {}
    )
}