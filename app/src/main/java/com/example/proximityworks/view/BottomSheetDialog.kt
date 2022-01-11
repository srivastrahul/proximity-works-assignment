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
import com.example.proximityworks.utilities.TimeFormatter
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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.robinhood.spark.SparkView
import javax.inject.Inject


@ExperimentalCoroutinesApi
class BottomSheetDialog @Inject constructor(): BottomSheetDialogFragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: BottomSheetDialogBinding
    private lateinit var sparkView: SparkView
    private lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_dialog, container, false)
        sparkView = binding.sparkView
        barChart = binding.barChart
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureChartAppearance()
        prepareChartData(viewModel.createBarData())
        sparkView.adapter = SparkLineAdapter(viewModel.getSelectedCityAqiArray())
    }

    private fun configureChartAppearance() {
        barChart.description.isEnabled = false
        barChart.setDrawValueAboveBar(false)

        val xAxis = barChart.xAxis
        xAxis.granularity = 10F
        xAxis.valueFormatter = IAxisValueFormatter { p0, _ ->
            TimeFormatter.convertTimestampTo12HourFormat(viewModel.getSelectedCityTimeStampArray()[p0.toInt()])
        }

        val yAxisLeft = barChart.axisLeft
        yAxisLeft.granularity = 50f
        yAxisLeft.axisMinimum = 0f

        val yAxisRight = barChart.axisRight
        yAxisRight.granularity = 50f
        yAxisRight.axisMinimum = 0f
    }

    private fun prepareChartData(data: BarData) {
        data.setValueTextSize(12f)
        data.barWidth = 0.5f
        barChart.data = data
        barChart.invalidate()
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

}