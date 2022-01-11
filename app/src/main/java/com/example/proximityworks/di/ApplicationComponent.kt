package com.example.proximityworks.di

import com.example.proximityworks.view.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(ViewModelModule::class), (NetworkModule::class)])
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)
}