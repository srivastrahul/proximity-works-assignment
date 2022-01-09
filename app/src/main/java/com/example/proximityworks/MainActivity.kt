package com.example.proximityworks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.proximityworks.databinding.ActivityMainBinding
import com.example.proximityworks.di.ViewModelFactory
import com.example.proximityworks.viewmodels.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: MainViewModel
    lateinit var mainActivityBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        BaseApplication.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        mainActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        setObservers()


    }

    override fun onResume() {
        super.onResume()
        mainActivityBinding.clickBtn.setOnClickListener {
            viewModel.subscribeToSocketEvents()
        }
    }

    private fun setObservers() {
        viewModel.getCityAqiMap().observe(this, {
            if (it.isNotEmpty())
                for (item in it.entries)
                    Log.i("${item.key}: ", "AQI: ${item.value.toString()}")
        })
    }
}