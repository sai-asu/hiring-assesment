package com.example.fetchexercise

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FetchExerciseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}