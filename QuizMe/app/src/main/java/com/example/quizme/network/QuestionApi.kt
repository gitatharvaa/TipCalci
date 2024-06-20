package com.example.quizme.network

import com.example.quizme.model.Question
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton

interface QuestionApi {
    @GET("world.json") //Get and go to this path/url
    suspend fun getAllQuestions(): Question
}