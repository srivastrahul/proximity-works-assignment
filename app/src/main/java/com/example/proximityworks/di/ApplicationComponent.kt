package com.example.proximityworks.di

import com.example.proximityworks.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(ViewModelModule::class)])
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)
}