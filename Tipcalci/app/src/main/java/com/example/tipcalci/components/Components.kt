@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tipcalci.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Inputfield(modifier: Modifier = Modifier,
               valueState: MutableState<String>,//Its suppose to hold value so that its reads and rights can be seen by the whole compose
               labelId: String,
               enabled:Boolean,
               isSingleLIne:Boolean, //allows only single line input
               keyboardType: KeyboardType = KeyboardType.Number,
               imeAction: ImeAction = ImeAction.Next, //shows the user what to do next after putting input.
               onAction: KeyboardActions = KeyboardActions.Default
               ){
    OutlinedTextField(value = valueState.value,
                    onValueChange ={valueState.value = it}, //what do we want as we update the text.
                    label = { Text(text = labelId)}, //Add the "Enter bill" text
                    leadingIcon = { Icon(imageVector = Icons.Rounded.AttachMoney, contentDescription ="Money Icon" )},
                    singleLine = isSingleLIne,
                    textStyle = TextStyle(fontSize = 18.sp,
                                            color = MaterialTheme.colorScheme.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(), //fill the outlined text field
                enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType,
                                            imeAction = imeAction),
        keyboardActions = onAction
    )




}