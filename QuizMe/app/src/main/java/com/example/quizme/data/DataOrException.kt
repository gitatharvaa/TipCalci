package com.example.quizme.data

data class DataOrException<T, Boolean, E: Exception> ( //wrapper class

    var data: T? = null,
    var loading: kotlin.Boolean? = null,
            var e: E? = null)
