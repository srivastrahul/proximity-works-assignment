package com.example.proximityworks.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.proximityworks.R
import com.example.proximityworks.adapter.SparkLineAdapter
import com.example.proximityworks.databinding.BottomSheetDialogBinding
import com.example.proximityworks.viewmodels.MainViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.datatype.DatatypeConstants.DAYS
import com.github.mikephil.charting.data.BarData
import com.robinhood.spark.SparkView


@ExperimentalCoroutinesApi
class BottomSheetDialog: BottomSheetDialogFragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: BottomSheetDialogBinding
    private lateinit var sparkView: SparkView
    //private lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_dialog, container, false)
        sparkView = binding.sparkView
        //barChart = binding.barChart
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sparkView.adapter = SparkLineAdapter(viewModel.getSelectedCityAqiArray())
        /*configureChartAppearance()
        prepareChartData(viewModel.createBarData())*/
    }

    /*private fun configureChartAppearance() {
        barChart.description.isEnabled = false
        barChart.setDrawValueAboveBar(false)

        val xAxis = barChart.xAxis
        xAxis.granularity = 1000000000000F
        xAxis.setValueFormatter { p0, _ ->
            val timeFormat = SimpleDateFormat("hh:mm aa", Locale.getDefault())
            timeFormat.format(p0.toInt())
        }

        val yAxisLeft = barChart.axisLeft
        yAxisLeft.granularity = 100f
        yAxisLeft.axisMinimum = 0f

        val yAxisRight = barChart.axisRight
        yAxisRight.granularity = 100f
        yAxisRight.axisMinimum = 0f
    }

    private fun prepareChartData(data: BarData) {
        data.setValueTextSize(12f)
        barChart.data = data
        barChart.invalidate()
    }*/

    companion object {
        const val TAG = "ModalBottomSheet"
    }

}