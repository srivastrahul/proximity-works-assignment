package com.example.proximityworks

import android.app.Application
import android.content.Context
import com.example.proximityworks.di.DaggerApplicationComponent

class BaseApplication: Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: BaseApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }


        val appComponent = DaggerApplicationComponent.create()

    }

}