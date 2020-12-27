package com.moham.digssquared.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.moham.digssquared.R
import com.moham.digssquared.data.entities.Values
import com.moham.digssquared.databinding.ActivityMainBinding
import com.moham.digssquared.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var disposable: Disposable

    //Vars
    private lateinit var chartRSRP: LineChart
    private lateinit var chartRSRQ: LineChart
    private lateinit var chartSINR: LineChart
    private lateinit var lineDataSetRSRP: LineDataSet
    private lateinit var lineDataSetRSRQ: LineDataSet
    private lateinit var lineDataSetSINR: LineDataSet
    private val dataValuesRSRP = arrayListOf<Entry>()
    private val dataValuesRSRQ = arrayListOf<Entry>()
    private val dataValuesSINR = arrayListOf<Entry>()
    private var time = 0f
    private var isLive = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupObservers()
        configureDisposable()
        configureChart()
        clicks()

    }

    private fun configureChart() {

        //RSRP Chart
        configureRSRPChart()
        configureRSRQChart()
        configureSINRChart()
    }


    private fun configureRSRPChart() {
        chartRSRP = binding.chartRSRP
        chartRSRP.xAxis.granularity = .25f
        chartRSRP.xAxis.axisMinimum = 0f
        chartRSRP.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chartRSRP.axisLeft.axisMinimum = -150f
        chartRSRP.axisLeft.granularity = 15f
        chartRSRP.axisRight.isEnabled = false
        chartRSRP.description.text= "Value of RSRP over time"
        lineDataSetRSRP = LineDataSet(dataValuesRSRP, "RSRP")
        lineDataSetRSRP.lineWidth = 3f
        lineDataSetRSRP.circleRadius = 5f
        lineDataSetRSRP.setDrawCircleHole(false)
        lineDataSetRSRP.setCircleColor(Color.BLUE)
        lineDataSetRSRP.color = Color.BLUE

    }

    private fun configureRSRQChart() {
        chartRSRQ = binding.chartRSRQ
        chartRSRQ.xAxis.granularity = .25f
        chartRSRQ.xAxis.axisMinimum = 0f
        chartRSRQ.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chartRSRQ.axisLeft.axisMinimum = -40f
        chartRSRQ.axisLeft.granularity = 5f
        chartRSRQ.axisRight.isEnabled = false
        chartRSRQ.description.text= "Value of RSRQ over time"
        lineDataSetRSRQ = LineDataSet(dataValuesRSRQ, "RSRQ")
        lineDataSetRSRQ.lineWidth = 3f
        lineDataSetRSRQ.circleRadius = 5f
        lineDataSetRSRQ.setDrawCircleHole(false)
        lineDataSetRSRQ.setCircleColor(Color.RED)
        lineDataSetRSRQ.color = Color.RED
    }

    private fun configureSINRChart() {
        chartSINR = binding.chartSINR
        chartSINR.xAxis.granularity = .25f
        chartSINR.xAxis.axisMinimum = 0f
        chartSINR.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chartSINR.axisLeft.axisMinimum = -20f
        chartSINR.axisLeft.granularity = 10f
        chartSINR.axisRight.isEnabled = false
        chartSINR.description.text= "Value of SINR over time"
        lineDataSetSINR = LineDataSet(dataValuesSINR, "SINR")
        lineDataSetSINR.lineWidth = 3f
        lineDataSetSINR.circleRadius = 5f
        lineDataSetSINR.setDrawCircleHole(false)
        lineDataSetSINR.setCircleColor(Color.GREEN)
        lineDataSetSINR.color = Color.GREEN
    }

    private fun configureDisposable() {
        disposable =
            Observable.interval(1000, 2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    viewModel.getValues()
                }
    }

    @SuppressLint("SetTextI18n")
    private fun clicks() {
        binding.btnGetValues.setOnClickListener {
            if(isLive){
                binding.tvStatusValue.text = "Offline"
                binding.tvStatusValue.setTextColor(resources.getColor(R.color.red,null))
                isLive = false
                binding.btnGetValues.text= "Get Live Values"
                disposable.dispose()
            }
            else{
                isLive = true
                binding.tvStatusValue.text = "Online"
                binding.tvStatusValue.setTextColor(resources.getColor(R.color.green,null))
                binding.btnGetValues.text= "Stop Live Values"
                configureDisposable()
            }
        }
    }

    private fun setupObservers() {
        viewModel.values.observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    handleSuccessResponse(it.data)
                }
                Resource.Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }

                Resource.Status.LOADING ->
                    binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun handleSuccessResponse(data: Values?) {
        binding.progressBar.visibility = View.GONE
        binding.rsrpView.apply {
            progressText = "${data?.RSRP}"
            progress = data?.getRSRPProgress()!!
            progressColor = data.getRSRPColor()
        }
        binding.rsrqView.apply {
            progressText = "${data?.RSRQ}"
            progress = data?.getRSRQProgress()!!
            progressColor = data.getRSRQColor()

        }
        binding.sinrView.apply {
            progressText = "${data?.SINR}"
            progress = data?.getSINRProgress()!!
            progressColor = data.getSINRColor()

        }

        handleRSRPChartUpdates(data)
        handleRSRQChartUpdates(data)
        handleSINRChartUpdates(data)

    }

    private fun handleRSRPChartUpdates(data: Values?) {
        dataValuesRSRP.add(Entry(time++, data?.RSRP?.toFloat()!!))
        lineDataSetRSRP.notifyDataSetChanged()
        val lineData = LineData(listOf(lineDataSetRSRP))
        chartRSRP.data = lineData
        chartRSRP.setVisibleXRangeMaximum(10f)
        chartRSRP.invalidate()
    }

    private fun handleRSRQChartUpdates(data: Values?) {
        dataValuesRSRQ.add(Entry(time++, data?.RSRQ?.toFloat()!!))
        lineDataSetRSRQ.notifyDataSetChanged()
        val lineData = LineData(listOf(lineDataSetRSRQ))
        chartRSRQ.data = lineData
        chartRSRQ.setVisibleXRangeMaximum(10f)
        chartRSRQ.invalidate()
    }

    private fun handleSINRChartUpdates(data: Values?) {
        dataValuesSINR.add(Entry(time++, data?.SINR?.toFloat()!!))
        lineDataSetSINR.notifyDataSetChanged()
        val lineData = LineData(listOf(lineDataSetSINR))
        chartSINR.data = lineData
        chartSINR.setVisibleXRangeMaximum(10f)
        chartSINR.invalidate()
    }

    override fun onResume() {
        super.onResume()
        if (disposable.isDisposed) {
            configureDisposable()
        }
    }

    override fun onPause() {
        super.onPause()
        disposable.dispose()
    }
}