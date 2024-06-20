package com.example.quizme

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizme.screens.QuestionViewModel
import com.example.quizme.screens.TriviaHome
import com.example.quizme.ui.theme.QuizMeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizMeTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    TriviaHome()
                }
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

}