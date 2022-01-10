package com.example.proximityworks

import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleObserver
import com.example.proximityworks.di.DaggerApplicationComponent

class BaseApplication: Application(), LifecycleObserver {


    companion object {
        val appComponent = DaggerApplicationComponent.create()
    }


}