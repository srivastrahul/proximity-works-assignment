package com.example.proximityworks

import android.app.Application
import com.example.proximityworks.di.DaggerApplicationComponent

class BaseApplication: Application() {
    val appComponent = DaggerApplicationComponent.create()
}