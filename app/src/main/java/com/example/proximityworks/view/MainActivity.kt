package com.example.proximityworks.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proximityworks.BaseApplication
import com.example.proximityworks.R
import com.example.proximityworks.adapter.CityAqiAdapter
import com.example.proximityworks.databinding.ActivityMainBinding
import com.example.proximityworks.di.ViewModelFactory
import com.example.proximityworks.utilities.ValidationCheckConstants
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

    override fun onStop() {
        super.onStop()
        viewModel.closeSocket()
    }

    private fun setObservers() {
        viewModel.getLastUpdatedCityAqiList().observe(this, {
            if (it.isNotEmpty())
                cityAqiAdapter.setData(it)
        })

        viewModel.getValidationCheck().observe(this, {
            when (it.type) {
                ValidationCheckConstants.NO_INTERNET, ValidationCheckConstants.EXCEPTION_NO_RESPONSE -> showToastMessage(it.message)
            }
        })
    }

    private fun setUpUI() {
        cityAqiRecyclerView = mainActivityBinding.cityAqiRV
        cityAqiAdapter = CityAqiAdapter(onClickLambda)
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        cityAqiRecyclerView.layoutManager = linearLayoutManager
        cityAqiRecyclerView.adapter = cityAqiAdapter
    }

    private val onClickLambda = { city: String? ->
        viewModel.selectedCity = city!!
        viewModel.selectedCityAqiList = viewModel.getCityAqiMap().value?.get(city)!!
        val bottomSheetDialog = BottomSheetDialog()
        bottomSheetDialog.show(supportFragmentManager, BottomSheetDialog.TAG)
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}