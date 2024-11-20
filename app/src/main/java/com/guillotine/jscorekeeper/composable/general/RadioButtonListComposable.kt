package com.guillotine.jscorekeeper.composable.general

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.guillotine.jscorekeeper.data.RadioButtonOptions

@Composable
fun RadioButtonList(
    currentSelectedOption: RadioButtonOptions,
    onOptionSelected: (RadioButtonOptions) -> Unit,
    listOfOptions: List<RadioButtonOptions>
) {
    Column() {
        HorizontalDivider()
        // selectableGroup used for accessibility reasons.
        Column(Modifier.selectableGroup()) {
            // Could I have stored this as a list in the XML? Probably. But I feel like it's going
            // to be nicer to handle this in the ViewModel as an enum than as a bunch of strings.
            listOfOptions.forEach { stringID ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (currentSelectedOption == stringID),
                            onClick = { onOptionSelected(stringID) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (currentSelectedOption == stringID),
                        // Set to null for accessibility reasons.
                        onClick = null
                    )
                    Text(
                        text = stringResource(stringID.stringResourceID),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
        HorizontalDivider()
    }
}