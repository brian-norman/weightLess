package com.example.weighttracker

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private val chartAdapter = ChartAdapter(emptyList())
    private val weightAdapter = WeightAdapter(emptyList()) { onWeightEntryClicked(it) }

    private val navController by lazy { this.findNavController() }

    private val weightDialogSharedViewModel by lazy {
        requireActivity().run {
            ViewModelProviders.of(this)[WeightDialogSharedViewModel::class.java]
        }
    }

    private val swipeHandler by lazy {
        object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedWeightEntry: WeightEntry = viewModel.removeWeightEntry(position)
                Snackbar.make(
                    viewHolder.itemView,
                    "Entry deleted!",
                    Snackbar.LENGTH_LONG
                ) .apply {
                    setAction("UNDO") { viewModel.addWeight(deletedWeightEntry) }
                    setActionTextColor(Color.YELLOW)
                    show()
                }
            }
        }
    }
    private val itemTouchHelper by lazy { ItemTouchHelper(swipeHandler) }

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
        itemTouchHelper.attachToRecyclerView(recyclerView)

        sparkView.setScrubListener {
            if (it != null) {
                val value: Float = it as Float
                message.text = value.toString()
            } else {
                message.text = getString(R.string.scrub_empty)
            }
        }

        addWeightButton.setOnClickListener {
            val action = MainFragmentDirections
                .actionMainFragmentToWeightDialogFragment(
                    weightEntryDate = 0.toLong(),
                    weightEntryWeight = 0f,
                    shouldEdit = false
                )
            navController.navigate(action)
        }

        viewModel.chartData.observe(viewLifecycleOwner, Observer {
            chartAdapter.setData(it.map { weightEntry ->  weightEntry.weight })
            weightAdapter.setData(it)
        })

        weightDialogSharedViewModel.newWeightEntry.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.addWeight(it)
            }
            weightDialogSharedViewModel.clearNewWeightEntry()
        })

        weightDialogSharedViewModel.editWeightEntry.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.updateWeight(it)
            }
            weightDialogSharedViewModel.clearEditWeightEntry()
        })
    }

    private fun onWeightEntryClicked(weightEntry: WeightEntry) {
        val action = MainFragmentDirections
            .actionMainFragmentToWeightDialogFragment(
                weightEntryDate = 0.toLong(),
                weightEntryWeight = weightEntry.weight,
                shouldEdit = true
            )
        navController.navigate(action)
    }

}
