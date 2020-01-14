package com.example.weighttracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private val chartAdapter = ChartAdapter(emptyList())
    private val weightAdapter = WeightAdapter(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        sparkView.adapter = chartAdapter
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = weightAdapter
        }

        sparkView.setScrubListener {
            if (it != null) {
                val value: Float = it as Float
                message.text = value.toString()
            } else {
                message.text = getString(R.string.scrub_empty)
            }
        }

        addWeightButton.setOnClickListener {
            viewModel.addNewWeight(100f)
        }

        viewModel.chartData.observe(viewLifecycleOwner, Observer {
            chartAdapter.setData(it ?: emptyList())
            weightAdapter.setData(it.map { weight -> WeightEntry("", weight) })
        })
    }

}
