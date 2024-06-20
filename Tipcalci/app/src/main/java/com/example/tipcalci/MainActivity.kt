@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.tipcalci

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalci.components.Inputfield
import com.example.tipcalci.ui.theme.TipcalciTheme
import com.example.tipcalci.util.calculateTotalPerPerson
import com.example.tipcalci.util.calculateTotalTip
import com.example.tipcalci.widgets.RoundIconButton


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Myapp{
                //TopContent()
                MainContent()
            }
        }
    }
}

@Composable
fun Myapp(content: @Composable () -> Unit){
    TipcalciTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colorScheme.primary) {
            content()
        }
    }
}

//@Preview
@Composable
fun TopContent(totalPerPerson: Double = 1345.0){
    Surface(modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
        .height(150.dp)
        .clip(shape = CircleShape.copy(all = CornerSize(25.dp))),
        color = Color(0xFFFFCCFF)
    ) {
        Column(modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            val Total = "%.2f".format(totalPerPerson)
            Text(color = Color.Black,
                text = "Total per person",
                style = MaterialTheme.typography.titleLarge)
            Text(color = Color.Black,
                text = "$$Total",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent(){
    val splitByState = remember {
        mutableStateOf(1)
    }
    val range = IntRange(start = 1, endInclusive = 50)

    val tipAmountState = remember {
        mutableStateOf(0.0)
    }
    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }

    Column {
        //topcontent

        BillForm(splitByState = splitByState,
            tipAmount = tipAmountState,
            totalPerPersonState = totalPerPersonState) {}
    }
}

@ExperimentalComposeUiApi
@Composable
fun BillForm(modifier: Modifier = Modifier,
             range: IntRange = 1..100,
             splitByState: MutableState<Int>,
             tipAmount: MutableState<Double>,
             totalPerPersonState: MutableState<Double>,
             onValChange: (String) -> Unit = {}
){

    val TotalBillState = remember {
        mutableStateOf("")
    }
    val validstate = remember(TotalBillState.value) {
        TotalBillState.value.trim().isNotEmpty()

    }
    val focusManager = LocalFocusManager.current

    val SliderPosState = remember {
        mutableStateOf(0f)
    }
    val tipPercentage = (SliderPosState.value * 100).toInt()

    val tipAmountState = remember {
        mutableStateOf(0.0)
    }



    TopContent(totalPerPerson = totalPerPersonState.value)

    Surface(modifier = modifier
        .padding(2.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(12.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ){
        Column(modifier = modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start) {

            Inputfield(valueState = TotalBillState,
                labelId = "Enter Total Bill",
                enabled = true,
                isSingleLIne = true,
                onAction = KeyboardActions {
                    if (!validstate) return@KeyboardActions
                    onValChange(TotalBillState.value.trim())


                    focusManager.clearFocus()
                })
             if(validstate) {
                 Row(
                     modifier = modifier.padding(3.dp),
                     horizontalArrangement = Arrangement.Start
                 ) {
                     Text(
                         text = "Split bill into",
                         modifier = Modifier.align(
                             alignment = Alignment.CenterVertically
                         )
                     )
                     Spacer(modifier = modifier.width(120.dp))
                     Row(
                         modifier = modifier.padding(horizontal = 3.dp),
                         horizontalArrangement = Arrangement.End
                     )
                     {
                         RoundIconButton(
                             imageVector = Icons.Default.Remove,
                             onClick = {
                                 splitByState.value =
                                     if (splitByState.value > 1) {
                                         splitByState.value - 1
                                     } else {
                                         1
                                     }
                                 totalPerPersonState.value =
                                     calculateTotalPerPerson(
                                         totalBill = TotalBillState.value.toDouble(),
                                         splitBy = splitByState.value,
                                         tipPercentage = tipPercentage
                                     )
                             })

                         Text(
                             fontSize = 18.sp,
                             text = " ${splitByState.value}",
                             modifier = modifier
                                 .align(Alignment.CenterVertically)
                                 .padding(start = 15.dp, end = 15.dp)
                         ) //spacing btn text and sign

                         RoundIconButton(
                             imageVector = Icons.Default.Add, //default button for plus
                             onClick = {
                                 if (splitByState.value < range.endInclusive) {
                                     splitByState.value = splitByState.value + 1
                                     totalPerPersonState.value =
                                         calculateTotalPerPerson(
                                             totalBill = TotalBillState.value.toDouble(),
                                             splitBy = splitByState.value,
                                             tipPercentage = tipPercentage
                                         )
                                 }

                             })


                     }
                 }
                 //Tip row
                 Row(
                     modifier = Modifier
                         .padding(horizontal = 3.dp, vertical = 9.dp)
                 )
                 {
                     Text(
                         text = "Tip amt",
                         modifier = Modifier.align(alignment = Alignment.CenterVertically)
                     )
                     Spacer(modifier = Modifier.width(182.dp)) //padding btn text and amount

                     //for Money amount

                     Text(
                         fontSize = 18.sp,
                         text = " $ ${tipAmountState.value}",
                         modifier = Modifier.align(alignment = Alignment.CenterVertically)
                     )

                 }
                 //}
                 Column(
                     verticalArrangement = Arrangement.Center,
                     horizontalAlignment = Alignment.CenterHorizontally
                 ) {
                     Text(
                         text = "${tipPercentage} %",
                         fontSize = 20.sp
                     )
                     Spacer(modifier = Modifier.height(14.dp))


                     //Slider
                     Slider(value = SliderPosState.value,
                         onValueChange = { newval ->
                             SliderPosState.value = newval

                             tipAmountState.value =
                                 calculateTotalTip(
                                     totalBill = TotalBillState.value.toDouble(),
                                     tipPercentage = tipPercentage
                                 )

                             totalPerPersonState.value =
                                 calculateTotalPerPerson(
                                     totalBill = TotalBillState.value.toDouble(),
                                     splitBy = splitByState.value,
                                     tipPercentage = tipPercentage
                                 )

                         },
                         modifier = Modifier.padding(
                             start = 16.dp,
                             end = 16.dp
                         ), //padding for slider
                         steps = 5,  //dots on line
                         onValueChangeFinished = {

                         })

                 }
             }else {
                  //box appears after putting value
             }

        }
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TipcalciTheme {
        TopContent()
    }
}