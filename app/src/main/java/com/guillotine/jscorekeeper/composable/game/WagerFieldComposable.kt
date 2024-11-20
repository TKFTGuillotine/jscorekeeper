package com.guillotine.jscorekeeper.composable.game

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.guillotine.jscorekeeper.R
import androidx.compose.ui.Modifier

@Composable
fun WagerFieldComposable (
    wagerText: String,
    setWagerText: (String) -> Unit,
    isShowError: Boolean,
    currency: String,
    modifier: Modifier = Modifier
) {
    TextField(
        modifier = modifier,
        value = wagerText,
        onValueChange = { setWagerText(it.filter{ it.isDigit() }) },
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