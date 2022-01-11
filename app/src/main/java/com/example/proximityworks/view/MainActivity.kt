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
import com.example.proximityworks.utilities.AppUtils
import com.example.proximityworks.utilities.ValidationCheckConstants
import com.example.proximityworks.viewmodels.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var viewModel: MainViewModel
    private lateinit var mainActivityBinding: ActivityMainBinding
    private lateinit var cityAqiRecyclerView: RecyclerView
    private lateinit var cityAqiAdapter: CityAqiAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager


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
        if (AppUtils.isInternetAvailable(this))
            viewModel.subscribeToSocketEvents()
        else
            showToastMessage(getString(R.string.no_internet))
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
           if (it == ValidationCheckConstants.EXCEPTION_NO_RESPONSE)
               showToastMessage(getString(R.string.something_went_wrong))
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
        bottomSheetDialog.show(supportFragmentManager, BottomSheetDialog.TAG)
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}