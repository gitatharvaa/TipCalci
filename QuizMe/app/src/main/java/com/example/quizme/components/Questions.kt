package com.example.quizme.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizme.model.QuestionsItem
import com.example.quizme.screens.QuestionViewModel
import com.example.quizme.utils.AppColours

@Composable
fun Questions(viewModel: QuestionViewModel = hiltViewModel()) {

    val questions = viewModel.data.value.data?.toMutableList()// data are passed through this to check if data is loaded or not

    val questionIndex = remember {
        mutableStateOf(0)
    }



    if(viewModel.data.value.loading == true) {
        CircularProgressIndicator()

    } else {
        val question = try {
            questions?.get(questionIndex.value)
        } catch (ex: Exception) {
            null
        }

       if (questions != null) {
           QuestionDisplay(question = question!!, questionIndex = questionIndex,
                                                                  viewModel){
               questionIndex.value += 1 // allow us to go to the next ques.
           }

       }
    }
}


@Composable
fun QuestionDisplay(
    question: QuestionsItem,//for variable options (1-4)
    questionIndex: MutableState<Int>,
    viewModel: QuestionViewModel,
    onNextClicked: (Int) -> Unit = {}//we pass the value of ques. that was just clicked.
) {

    val choiceState = remember(question) { //user choose the option is stored here(option's index value)
        question.choices.toMutableList()
    }

    val answerState = remember(question) {
        mutableStateOf<Int?>(null)
    }

    val correctAnswerState = remember(question) {
        mutableStateOf<Boolean?>(null)
    }


    val updateAnswer: (Int) -> Unit = remember(question) {
        {
            answerState.value = it //it to control our index value
            correctAnswerState.value = choiceState[it] == question.answer //check/compare the answer with the choices
        }
    }

    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)//This creates (- - - ) pattern,
                                                    // where 10f,10f are dash and 0f is space

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = AppColours.mDarkPurple
    ) {
        
        Column(
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 50.dp)
                .padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start) {

            if (questionIndex.value >= 1){
                ShowProgress(score = questionIndex.value)
            }

            QuestionTracker(counter = questionIndex.value, viewModel.getTotalQuestionCount())
            DrawDottedLine(pathEffect)
            Spacer(modifier = Modifier.padding(8.dp))

            Column {
                Text(text = question.question,
                    modifier = Modifier
                        .padding(6.dp)
                        .align(alignment = Alignment.Start)
                        .fillMaxHeight(0.3f),//this is imp, cause there are diff que. with diff
                                            // length
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold,
                    color = AppColours.mOffWhite,
                    lineHeight = 22.sp)

                //choices
                choiceState.forEachIndexed { index, answerText ->
                    Row (modifier = Modifier
                        .padding(horizontal = 0.dp, vertical = 8.dp)
                        .padding(3.dp)
                        .fillMaxWidth()
                        .height(55.dp)
                        .border(
                            width = 4.dp, brush = Brush.linearGradient(
                                colors = listOf(
                                    AppColours.mLightCyan,
                                    AppColours.mOffDarkPurple
                                )
                            ),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .clip(
                            RoundedCornerShape(
                                topStartPercent = 50,
                                topEndPercent = 50,
                                bottomEndPercent = 50,
                                bottomStartPercent = 50
                            )
                        )
                        .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically){

                        RadioButton(selected = (answerState.value == index),
                            onClick = {
                                updateAnswer(index)//will update the index to check all options
                            },
                            modifier = Modifier.padding(16.dp),
                            colors = RadioButtonDefaults
                                .colors(
                                    selectedColor = if (correctAnswerState.value == true &&
                                        index == answerState.value) {
                                        Color.Green  //.copy(alpha = 0.2f)
                                    } else if (correctAnswerState.value == false
                                        && index == answerState.value){
                                        Color.Red
                                        }else{
                                        AppColours.mOffWhite
                                    },
                                ))//end radio button
                        val annotatedString = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Light,
                                                        color = if (correctAnswerState.value == true
                                                            &&index == answerState.value) {
                                                            Color.Green
                                                        }else if(correctAnswerState.value == false
                                                            && index == answerState.value) {
                                                            Color.Red
                                                        }else{
                                                            AppColours.mOffWhite},
                                fontSize = 17.sp)){
                                append(answerText)
                            }
                        }
                        Text(text = annotatedString, modifier = Modifier.padding(6.dp))
                    }
                }
                
                Spacer(modifier = Modifier.padding(10.dp))
                
                Button(onClick = { onNextClicked(questionIndex.value) },
                    modifier = Modifier
                        .padding(3.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(34.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColours.mLightBlue)) {
                    Text(text = "Next",
                        modifier = Modifier.padding(4.dp),
                        color = AppColours.mOffWhite,
                        fontSize = 17.sp)
                }

            }


        }
    }
}


@Composable
fun DrawDottedLine(pathEffect: PathEffect) {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp),) {
        drawLine(color = AppColours.mLightGray,
            start = Offset(0f, 0f),//it stays at one point
            end = Offset(size.width, y = 0f),
            pathEffect = pathEffect)

    }

}


@Preview
@Composable
fun ShowProgress(score: Int = 12){

    val gradient = Brush.linearGradient(listOf(Color(0xfff95075),
                                               Color(0xffbe6be5)))

    val progressFactor by remember(score) {
        mutableStateOf(score*0.005f)
    }


    Row (modifier = Modifier
        .padding(3.dp)
        .fillMaxWidth()
        .height(45.dp)
        .border(
            width = 4.dp, brush = Brush.linearGradient(
                colors = listOf(
                    AppColours.mLightPurple,
                    AppColours.mLightPurple
                )
            ),
            shape = RoundedCornerShape(34.dp)
        )
        .clip(
            RoundedCornerShape(
                topStartPercent = 50,
                topEndPercent = 50,
                bottomEndPercent = 50,
                bottomStartPercent = 50
            )
        )
        .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically){
        Button(
            contentPadding = PaddingValues(1.dp),
            onClick = { },
            modifier = Modifier
                .fillMaxWidth(progressFactor)
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent)) {

            Text(text = (score * 10).toString(),
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(23.dp))
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .padding(6.dp),
                color = AppColours.mOffWhite,
                textAlign = TextAlign.Center)

        }

    }
}


@Preview
@Composable
fun QuestionTracker(counter: Int = 10,
                    outOf: Int = 100){
    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
            withStyle(style = SpanStyle(color = AppColours.mLightGray,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 27.sp)){
                append("Question $counter/")
                withStyle(style = SpanStyle(color = AppColours.mLightGray,
                                                fontWeight = FontWeight.Light,
                                                fontSize = 18.sp)){
                    append("$outOf")
                }

            }
        }
        },
        modifier = Modifier.padding(20.dp))
}