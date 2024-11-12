package com.guillotine.jscorekeeper.composable.game

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.data.ClueDialogState
import com.guillotine.jscorekeeper.ui.theme.ClueCardTheme

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
        },
        // Somehow, the AlertDialog does *not* default to proper M3 spec dialog colors,
        // so this will enforce that.
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh

    )
}

@Composable
fun ClueDialog(
    onDismissRequest: () -> Unit,
    value: Int,
    currency: String,
    isRemainingDailyDouble: Boolean,
    onDailyDouble: () -> Unit,
    isWagerValid: (Int) -> Boolean,
    clueDialogState: ClueDialogState,
    onCorrect: (Int) -> Boolean,
    onIncorrect: (Int) -> Boolean,
    onPass: (Int) -> Unit,
    onNoMoreDailyDoubles: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                // Per M3 spec, the largest possible width.
                .width(560.dp)
                .height(IntrinsicSize.Max)
                .animateContentSize(),
            // Per M3 spec, the correct amount of roundness.
            shape = RoundedCornerShape(28.dp),
            // Per M3 spec, the correct dialog color.
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            when (clueDialogState) {
                ClueDialogState.MAIN -> ClueDialogMainContents(
                    onDismissRequest = onDismissRequest,
                    value = value,
                    currency = currency,
                    isRemainingDailyDouble = isRemainingDailyDouble,
                    onDailyDouble = { onDailyDouble() },
                    onCorrect = onCorrect,
                    onIncorrect = onIncorrect,
                    onPass = onPass
                )

                ClueDialogState.DAILY_DOUBLE_WAGER -> ClueDialogWagerContents(
                    currency = currency,
                    isWagerValid = isWagerValid,
                    onDismissRequest = onDismissRequest
                )

                ClueDialogState.DAILY_DOUBLE_RESPONSE -> ClueDialogResponseContents(
                    currency = currency,
                    value = value,
                    onDismissRequest = onDismissRequest,
                    onCorrect = onCorrect,
                    onIncorrect = onIncorrect,
                    onNoMoreDailyDoubles = {onNoMoreDailyDoubles()}
                )

                ClueDialogState.NONE -> Unit
            }
        }
    }
}

@Composable
fun ClueDialogMainContents(
    onDismissRequest: () -> Unit,
    value: Int,
    currency: String,
    isRemainingDailyDouble: Boolean,
    onDailyDouble: () -> Unit,
    onCorrect: (Int) -> Boolean,
    onIncorrect: (Int) -> Boolean,
    onPass: (Int) -> Unit,
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
            ClueCardTheme {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp).fillMaxWidth(),
                        text = "${currency}${value}",
                        style = MaterialTheme.typography.headlineSmall,
                        // Again, this breaks spec, but in this case I think it looks best this way.
                        textAlign = TextAlign.Center

                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                onClick = {onCorrect(value)},
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = stringResource(R.string.correct),
                )
            }
            TextButton(
                onClick = {onIncorrect(value)},
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
                .padding(bottom = 24.dp),
            // This is not to spec, but that's merely because I think this will be more usable.
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = {onPass(value)},
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = stringResource(R.string.pass),
                )
            }
            TextButton(
                onClick = { onDailyDouble() },
                modifier = Modifier.weight(1f),
                enabled = isRemainingDailyDouble,
            ) {
                Text(
                    text = stringResource(R.string.daily_double)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { onDismissRequest() },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    }
}

@Composable
fun ClueDialogWagerContents(
    currency: String,
    isWagerValid: (Int) -> Boolean,
    onDismissRequest: () -> Unit
) {
    var wagerText by remember { mutableStateOf("") }
    var isShowError by remember { mutableStateOf(false) }
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
                // Per M3 spec, the correct amount of title padding.
                .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.daily_double),
                // Per M3 spec, the correct style.
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = wagerText,
                onValueChange = { wagerText = it.filter { it.isDigit() } },
                label = { Text(text = stringResource(R.string.enter_wager)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                isError = isShowError,
                prefix = { Text(text = currency) },
                supportingText = {
                    if (isShowError) {
                        Text(text = stringResource(R.string.wager_invalid))
                    }
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text(text = stringResource(R.string.cancel))
            }
            TextButton(
                onClick = {
                    isShowError = !isWagerValid(wagerText.toInt())
                }
            ) {
                Text(text = stringResource(R.string.submit))
            }
        }
    }
}

@Composable
fun ClueDialogResponseContents(
    currency: String,
    value: Int,
    onDismissRequest: () -> Unit,
    onIncorrect: (Int) -> Boolean,
    onCorrect: (Int) -> Boolean,
    onNoMoreDailyDoubles: () -> Unit
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
                // Per M3 spec, the correct amount of title padding.
                .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.were_you_correct),
                // Per M3 spec, the correct style
                style = MaterialTheme.typography.headlineSmall,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${stringResource(R.string.wager)}: ${currency}${value}",
                // Per M3 spec, the correct style.
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text(text = stringResource(R.string.cancel))
            }
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { if (!onIncorrect(value)) {onNoMoreDailyDoubles()} }
                ) {
                    Text(text = stringResource(R.string.incorrect))
                }
                TextButton(
                    onClick = { if (!onCorrect(value)) {onNoMoreDailyDoubles()} }
                ) {
                    Text(text = stringResource(R.string.correct))
                }
            }
        }
    }
}

/*
@Preview
@Composable
fun NextRoundDialogPreview() {
    NextRoundDialog(onDismissRequest = {}, onConfirmation = {})
}

@Preview
@Composable
fun ClueDialogMainPreview() {
    Dialog(onDismissRequest = { }) {
        Card(
            modifier = Modifier
                // Per M3 spec, the largest possible width.
                .width(560.dp)
                .height(IntrinsicSize.Max)
                .animateContentSize(),
            // Per M3 spec, the correct amount of roundness.
            shape = RoundedCornerShape(28.dp),
        ) {
            ClueDialogMainContents(
                onDismissRequest = {},
                value = 200,
                currency = "$",
                isRemainingDailyDouble = false,
                onDailyDouble = {},
                onCorrect = {},
                onIncorrect = {},
                onPass = {}
            )

        }
    }
}

@Preview
@Composable
fun ClueDialogWagerValidPreview() {
    Dialog(onDismissRequest = { }) {
        Card(
            modifier = Modifier
                // Per M3 spec, the largest possible width.
                .width(560.dp)
                .height(IntrinsicSize.Max)
                .animateContentSize(),
            // Per M3 spec, the correct amount of roundness.
            shape = RoundedCornerShape(28.dp),
        ) {
            ClueDialogWagerContents(
                currency = "$",
                isWagerValid = { true },
                onDismissRequest = {}
            )

        }
    }
}

@Preview
@Composable
fun ClueDialogWagerInvalidPreview() {
    Dialog(onDismissRequest = { }) {
        Card(
            modifier = Modifier
                // Per M3 spec, the largest possible width.
                .width(560.dp)
                .height(IntrinsicSize.Max)
                .animateContentSize(),
            // Per M3 spec, the correct amount of roundness.
            shape = RoundedCornerShape(28.dp),
        ) {
            ClueDialogWagerContents(
                currency = "$",
                isWagerValid = { false },
                onDismissRequest = {}
            )
        }

    }
}

@Preview
@Composable
fun ClueDialogResponsePreview() {
    Dialog(onDismissRequest = { }) {
        Card(
            modifier = Modifier
                // Per M3 spec, the largest possible width.
                .width(560.dp)
                .height(IntrinsicSize.Max)
                .animateContentSize(),
            // Per M3 spec, the correct amount of roundness.
            shape = RoundedCornerShape(28.dp),
        ) {
            ClueDialogResponseContents(
                currency = "$",
                value = 200,
                onDismissRequest = {},
                onCorrect = {},
                onIncorrect = {}
            )
        }

    }
}*/
