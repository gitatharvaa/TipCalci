package com.example.quizme

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


/* this is our application level class which will govern this entire
application and give hilt the capacity of binding all dependencies
in the application */
@HiltAndroidApp
class TriviaApplication: Application() {}