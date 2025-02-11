package com.guillotine.jscorekeeper.composable.game

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.composable.general.ClueCardComposable
import com.guillotine.jscorekeeper.composable.general.RadioButtonList
import com.guillotine.jscorekeeper.composable.general.WagerFieldComposable
import com.guillotine.jscorekeeper.data.RadioButtonOptions
import com.guillotine.jscorekeeper.data.ClueDialogState

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
    onOptionSelected: (RadioButtonOptions) -> Unit,
    currentSelectedOption: RadioButtonOptions,
    onDismissRequest: () -> Unit,
    value: Int,
    currency: String,
    listOfOptions: List<RadioButtonOptions>,
    onDailyDouble: () -> Unit,
    isWagerValid: (Int) -> Boolean,
    clueDialogState: ClueDialogState,
    onCorrect: (Int) -> Boolean,
    onIncorrect: (Int) -> Boolean,
    onPass: (Int) -> Unit,
    onNoMoreDailyDoubles: () -> Unit,
    wagerText: String,
    setWagerText: (String) -> Unit,
    isShowError: Boolean,
    setIsShowError: (Boolean) -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                // Per M3 spec, the largest possible width.
                .width(560.dp)
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
                    onOptionSelected,
                    currentSelectedOption,
                    onDismissRequest = onDismissRequest,
                    value = value,
                    currency = currency,
                    listOfOptions = listOfOptions,
                    onDailyDouble = { onDailyDouble() },
                    onCorrect = onCorrect,
                    onIncorrect = onIncorrect,
                    onPass = onPass
                )

                ClueDialogState.DAILY_DOUBLE_WAGER -> ClueDialogWagerContents(
                    currency = currency,
                    isWagerValid = isWagerValid,
                    onDismissRequest = onDismissRequest,
                    wagerText = wagerText,
                    setWagerText = setWagerText,
                    isShowError = isShowError,
                    setIsShowError = setIsShowError
                )

                ClueDialogState.DAILY_DOUBLE_RESPONSE -> ClueDialogResponseContents(
                    currency = currency,
                    value = value,
                    onDismissRequest = onDismissRequest,
                    onCorrect = onCorrect,
                    onIncorrect = onIncorrect,
                    onNoMoreDailyDoubles = { onNoMoreDailyDoubles() }
                )

                ClueDialogState.NONE -> Unit
            }
        }
    }
}

@Composable
fun ClueDialogMainContents(
    onOptionSelected: (RadioButtonOptions) -> Unit,
    currentSelectedOption: RadioButtonOptions,
    onDismissRequest: () -> Unit,
    value: Int,
    currency: String,
    listOfOptions: List<RadioButtonOptions>,
    onDailyDouble: () -> Unit,
    onCorrect: (Int) -> Boolean,
    onIncorrect: (Int) -> Boolean,
    onPass: (Int) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(IntrinsicSize.Max)
            // Per M3 spec, the correct amount of perimeter padding.
            .padding(24.dp)
    ) {
        // Header/Card row
        Row(
            modifier = Modifier
                // Seems like the smallest I can get it.
                .height(90.dp)
                .fillMaxWidth()
                // Per M3 spec, the correct amount of title padding.
                .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 16.dp),
            // Per M3 spec, titles are left aligned if there is no icon. That said, I've
            // modified enough here that I think that looks ugly as sin, so I'm centering
            // it in this case.
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ClueCardComposable(currency = currency, value = value)
        }
        // Options row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButtonList(
                currentSelectedOption = currentSelectedOption,
                onOptionSelected = onOptionSelected,
                listOfOptions = listOfOptions
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // Per M3 spec, 40dp is the height of a button, +24.dp for the padding.
                .height(64.dp)
                // Per M3 spec, the correct amount of padding for the bottom button row.
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            var resizeFirstButton by remember { mutableStateOf(false) }
            var resizeSecondButton by remember { mutableStateOf(false) }
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    maxLines = 1,
                    onTextLayout = { result ->
                        if (result.hasVisualOverflow) {
                            resizeFirstButton = true
                        }
                    },
                    style = if (resizeFirstButton) {
                        MaterialTheme.typography.labelSmall
                    } else {
                        // This is the standard type for a Text Button according to the M3 spec.
                        MaterialTheme.typography.labelLarge
                    }
                )
            }
            TextButton(
                onClick = {
                    when (currentSelectedOption) {
                        RadioButtonOptions.CORRECT -> onCorrect(value)
                        RadioButtonOptions.INCORRECT -> onIncorrect(value)
                        RadioButtonOptions.PASS -> onPass(value)
                        RadioButtonOptions.DAILY_DOUBLE -> onDailyDouble()
                    }
                }
            ) {
                Text(
                    text = stringResource(R.string.confirm_continue),
                    maxLines = 1,
                    onTextLayout = { result ->
                        if (result.hasVisualOverflow) {
                            resizeSecondButton = true
                        }
                    },
                    style = if (resizeSecondButton) {
                        MaterialTheme.typography.labelSmall
                    } else {
                        // This is the standard type for a Text Button according to the M3 spec.
                        MaterialTheme.typography.labelLarge
                    }
                )
            }
        }
    }
}

@Composable
fun ClueDialogWagerContents(
    currency: String,
    isWagerValid: (Int) -> Boolean,
    wagerText: String,
    setWagerText: (String) -> Unit,
    isShowError: Boolean,
    setIsShowError: (Boolean) -> Unit,
    onDismissRequest: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(IntrinsicSize.Max)
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
            WagerFieldComposable(
                wagerText = wagerText,
                setWagerText = setWagerText,
                isShowError = isShowError,
                currency = currency
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
                    setIsShowError(!isWagerValid(wagerText.toInt()))
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
            .height(IntrinsicSize.Max)
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
                    onClick = {
                        if (!onIncorrect(value)) {
                            onNoMoreDailyDoubles()
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.incorrect))
                }
                TextButton(
                    onClick = {
                        if (!onCorrect(value)) {
                            onNoMoreDailyDoubles()
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.correct))
                }
            }
        }
    }
}

fun dummyCallback(arg: Int): Boolean {
    return true
}

@Preview
@Composable
fun ClueDialogOptionsListPreview() {
    RadioButtonList(
        currentSelectedOption = RadioButtonOptions.CORRECT,
        onOptionSelected = {},
        listOfOptions = RadioButtonOptions.entries
    )
}

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
                listOfOptions = RadioButtonOptions.entries,
                onDailyDouble = {},
                // :: is a function reference operator, allowing the passage of this function as an
                // argument.
                onCorrect = ::dummyCallback,
                onIncorrect = ::dummyCallback,
                onPass = ::dummyCallback,
                currentSelectedOption = RadioButtonOptions.CORRECT,
                onOptionSelected = {}
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
                onDismissRequest = {},
                setIsShowError = {},
                wagerText = "",
                setWagerText = {},
                isShowError = false
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
                onDismissRequest = {},
                setIsShowError = {},
                wagerText = "",
                setWagerText = {},
                isShowError = true
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
                onCorrect = ::dummyCallback,
                onIncorrect = ::dummyCallback,
                onNoMoreDailyDoubles = {}
            )
        }

    }
}
