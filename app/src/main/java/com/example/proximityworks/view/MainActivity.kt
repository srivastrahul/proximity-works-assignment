package com.example.proximityworks.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proximityworks.BaseApplication
import com.example.proximityworks.R
import com.example.proximityworks.adapter.CityAqiAdapter
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
    lateinit var cityAqiRecyclerView: RecyclerView
    lateinit var cityAqiAdapter: CityAqiAdapter
    lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        BaseApplication.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        mainActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        setUpUI()
        setObservers()

    }

    override fun onStart() {
        super.onStart()
        viewModel.subscribeToSocketEvents()
    }

    private fun setObservers() {
        viewModel.getLastUpdatedCityAqiList().observe(this, {
            if (it.isNotEmpty())
                cityAqiAdapter.setData(it)
        })
    }

    private fun setUpUI() {
        cityAqiRecyclerView = mainActivityBinding.cityAqiRV
        cityAqiAdapter = CityAqiAdapter()
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        cityAqiRecyclerView.layoutManager = linearLayoutManager
        cityAqiRecyclerView.adapter = cityAqiAdapter
    }
}