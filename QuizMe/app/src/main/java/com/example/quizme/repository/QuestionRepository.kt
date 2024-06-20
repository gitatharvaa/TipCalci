package com.example.quizme.repository

import android.util.Log
import com.example.quizme.data.DataOrException
import com.example.quizme.model.QuestionsItem
import com.example.quizme.network.QuestionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(
        private val api: QuestionApi) {
        /* The process of wrapping our data into another class that allows us to
add more data to that class
so we can unwrap and access diff. parts of data
it's part of clean architecture
 */
        private val dataOrException = DataOrException<ArrayList<QuestionsItem>,
                Boolean,
                Exception>()

        suspend fun getAllQuestions(): DataOrException<ArrayList<QuestionsItem>,
                Boolean,
                java.lang.Exception> {
        try {
              dataOrException.loading = true //for user to wait if above conditions are true
                dataOrException.data = api.getAllQuestions()
                if (dataOrException.data.toString().isNotEmpty()) {//if data is not empty don't show buffering
                        dataOrException.loading = false
                }
        }catch (exception: Exception){
                dataOrException.e = exception
                Log.d("Exc", "getAllQuestions: ${dataOrException.e!!.localizedMessage}")
        }
                return dataOrException
        }
}