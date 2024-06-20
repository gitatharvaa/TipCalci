package com.example.quizme.di

import com.example.quizme.network.QuestionApi
import com.example.quizme.repository.QuestionRepository
import com.example.quizme.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//here internal connections is made with the JSON
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideQuestionRepository(api: QuestionApi) = QuestionRepository(api)



    @Singleton
    @Provides //whole proj. will have access to this dependency
    fun provideQuestionApi(): QuestionApi{ //this provides dependency to anyone who needs it
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())//converts JSON into serialized objects
            .build()
            .create(QuestionApi ::class.java)

    }
}