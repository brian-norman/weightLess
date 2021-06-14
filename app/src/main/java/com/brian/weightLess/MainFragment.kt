package com.brian.weightLess

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brian.weightLess.data.AppDatabase
import com.brian.weightLess.data.WeightEntity
import com.brian.weightLess.data.getDate
import com.brian.weightLess.databinding.MainFragmentBinding
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding: MainFragmentBinding get() = _binding!!

    private val viewModel: MainViewModel by viewModels {
        MainViewModel(AppDatabase(requireContext()).weightDao())
    }
    private val sharedViewModel: WeightDialogSharedViewModel by viewModels( {requireActivity()} )

    private val chartAdapter = ChartAdapter(emptyList())
    private val weightAdapter = WeightAdapter { onWeightEntryClicked(it) }

    private val navController by lazy { this.findNavController() }

    private val swipeHandler by lazy {
        object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedWeightEntity: WeightEntity = viewModel.deleteWeight(position)
                Snackbar.make(
                    viewHolder.itemView,
                    getString(R.string.weight_deleted),
                    Snackbar.LENGTH_LONG
                ) .apply {
                    setAction(getString(R.string.undo)) { viewModel.insertWeight(deletedWeightEntity) }
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
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setTitle(R.string.app_name)

        binding.sparkView.adapter = chartAdapter
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = weightAdapter
        }
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        binding.sparkView.setScrubListener {
            if (it != null) {
                val weightEntity = it as WeightEntity
                binding.message.text = getString(R.string.scrubber_label_lbs, weightEntity.pounds.toString(), weightEntity.getDate())
            } else {
                val weights = viewModel.weightEntities.value ?: emptyList()
                binding.message.text = if (weights.size < 2) getString(R.string.empty_state) else getString(R.string.scrub_empty)
            }
        }

        binding.addWeightButton.setOnClickListener {
            val action = MainFragmentDirections
                .actionMainFragmentToWeightDialogFragment(
                    weightEntityDate = 0.toLong(),
                    weightEntityWeight = 0f,
                    shouldEdit = false
                )
            navController.navigate(action)
        }

        viewModel.weightEntities.observe(viewLifecycleOwner, {
            val weights = it ?: emptyList()
            chartAdapter.setData(weights.reversed())  // Most recent entry at the very end on chart
            weightAdapter.setData(weights)
            binding.message.text = if (weights.size < 2) getString(R.string.empty_state) else getString(R.string.scrub_empty)
        })

        sharedViewModel.newWeightEntity.observe(viewLifecycleOwner, { weightEntity ->
            weightEntity?.let { newWeightEntity ->
                val currentWeights = viewModel.weightEntities.value ?: listOf()
                val collision = currentWeights.filter { it.date == newWeightEntity.date }
                if (collision.isNotEmpty()) {
                    viewModel.updateWeight(collision[0].copy(pounds = newWeightEntity.pounds))
                    Toast.makeText(context, "Updated existing entry with that date", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.insertWeight(weightEntity)
                }
            }
        })

        sharedViewModel.editWeightEntity.observe(viewLifecycleOwner, { weightEntity ->
            weightEntity?.let { editedWeightEntity ->
                val currentWeights = viewModel.weightEntities.value!!
                val collision = currentWeights.filter { it.date == editedWeightEntity.date && it.id != editedWeightEntity.id }
                if (collision.isNotEmpty()) {
                    viewModel.updateWeight(collision[0].copy(pounds = editedWeightEntity.pounds))
                    viewModel.deleteWeight(editedWeightEntity)
                    Toast.makeText(context, "Updated existing entry with that date", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.updateWeight(editedWeightEntity)
                }
            }
        })
    }

    private fun onWeightEntryClicked(weightEntity: WeightEntity) {
        val action = MainFragmentDirections
            .actionMainFragmentToWeightDialogFragment(
                weightEntityId = weightEntity.id,
                weightEntityDate = weightEntity.date,
                weightEntityWeight = weightEntity.pounds,
                shouldEdit = true
            )
        navController.navigate(action)
    }
}
