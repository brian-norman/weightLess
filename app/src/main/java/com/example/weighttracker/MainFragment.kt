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
import com.example.weighttracker.data.AppDatabase
import com.example.weighttracker.data.WeightEntity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    private val viewModel by lazy { MainViewModel(AppDatabase(requireContext()).weightDao()) }

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
                val deletedWeightEntity: WeightEntity = viewModel.deleteWeight(position)
                Snackbar.make(
                    viewHolder.itemView,
                    "Entry deleted!",
                    Snackbar.LENGTH_LONG
                ) .apply {
                    setAction("UNDO") { viewModel.insertWeight(deletedWeightEntity) }
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
        toolbar.setTitle(R.string.app_name)

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
                    weightEntityDate = 0.toLong(),
                    weightEntityWeight = 0f,
                    shouldEdit = false
                )
            navController.navigate(action)
        }

        viewModel.weightEntities.observe(viewLifecycleOwner, Observer {
            chartAdapter.setData(it.map { weightEntity ->  weightEntity.weight })
            weightAdapter.setData(it)
        })

        weightDialogSharedViewModel.newWeightEntity.observe(viewLifecycleOwner, Observer { weightEntity ->
            weightEntity?.let { newWeightEntity ->
                val currentWeights = viewModel.weightEntities.value ?: listOf()
                val collision = currentWeights.filter { it.date == newWeightEntity.date }
                if (collision.isNotEmpty()) {
                    viewModel.updateWeight(collision[0].copy(weight = newWeightEntity.weight))
                } else {
                    viewModel.insertWeight(weightEntity)
                }
            }
            weightDialogSharedViewModel.clearNewWeightEntity()
        })

        weightDialogSharedViewModel.editWeightEntity.observe(viewLifecycleOwner, Observer { weightEntity ->
            weightEntity?.let { editedWeightEntity ->
                val currentWeights = viewModel.weightEntities.value!!
                val collision = currentWeights.filter { it.date == editedWeightEntity.date }
                if (collision.isNotEmpty()) {
                    viewModel.updateWeight(collision[0].copy(weight = editedWeightEntity.weight))
                    viewModel.deleteWeight(editedWeightEntity)
                } else {
                    viewModel.updateWeight(editedWeightEntity)
                }
            }
            weightDialogSharedViewModel.clearEditWeightEntity()
        })
    }

    private fun onWeightEntryClicked(weightEntity: WeightEntity) {
        val action = MainFragmentDirections
            .actionMainFragmentToWeightDialogFragment(
                weightEntityId = weightEntity.id,
                weightEntityDate = weightEntity.date,
                weightEntityWeight = weightEntity.weight,
                shouldEdit = true
            )
        navController.navigate(action)
    }

}
