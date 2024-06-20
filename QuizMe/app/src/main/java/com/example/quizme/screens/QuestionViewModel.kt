package com.example.quizme.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizme.data.DataOrException
import com.example.quizme.model.QuestionsItem
import com.example.quizme.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val repository: QuestionRepository)
    : ViewModel() {
    //for composable we need state, so we created data class
    val data: MutableState<DataOrException<ArrayList<QuestionsItem>,
            Boolean,
            Exception>> = mutableStateOf(
                DataOrException(null, true, Exception("")) )

    init {
        getAllQuestions()
    }

    private fun getAllQuestions(){
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllQuestions()
            if (data.value.data.toString().isNotEmpty()){
                data.value.loading = false
            }
        }
    }

    fun getTotalQuestionCount(): Int {
        return data.value.data?.toMutableList()?.size!!
    }

}