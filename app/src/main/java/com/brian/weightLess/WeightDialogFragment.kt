package com.brian.weightLess

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.brian.weightLess.data.WeightEntity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.dialog_weight_fragment.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class WeightDialogFragment: DialogFragment() {

    private val args: WeightDialogFragmentArgs by navArgs()

    private val sharedViewModel: WeightDialogSharedViewModel by viewModels( {requireActivity()} )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onStart() {
        super.onStart()

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog!!.window!!.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_weight_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.weightEntityDate != 0.toLong()) {
            dateTextInputEditText.setText(
                SimpleDateFormat("MMM d, yyyy", Locale.US)
                    .format(Date.from(Instant.ofEpochSecond(args.weightEntityDate)))
            )
        }
        if (args.weightEntityWeight != 0f) {
            weightTextInputEditText.setText(args.weightEntityWeight.toString())
        }

        initDatePicker()

        toolbar.apply {
            title = if (args.shouldEdit) getString(R.string.edit_weight) else getString(R.string.new_weight)
            setNavigationOnClickListener { dismiss() }
            inflateMenu(R.menu.weight_dialog)
            setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.action_save -> {
                        saveWeightEntry()
                    }
                }
                dismiss()
                true
            }
        }
    }

    private fun saveWeightEntry() {
        if (args.shouldEdit) {
            sharedViewModel.saveEditWeightEntity(
                WeightEntity(
                    id = args.weightEntityId,
                    date = SimpleDateFormat("MMM d, yyyy", Locale.US).parse(dateTextInputEditText.text.toString()).toInstant().epochSecond,
                    pounds = weightTextInputEditText.toFloatOrZero()
                )
            )

        } else {
            sharedViewModel.saveNewWeightEntity(
                WeightEntity(
                    date = SimpleDateFormat("MMM d, yyyy", Locale.US).parse(dateTextInputEditText.text.toString()).toInstant().epochSecond,
                    pounds = weightTextInputEditText.toFloatOrZero()
                )
            )
        }
    }

    private fun initDatePicker() {

        if (dateTextInputEditText.text.isNullOrEmpty()) {
            dateTextInputEditText.setText(
                SimpleDateFormat("MMM d, yyyy", Locale.US).format(
                    Date.from(Instant.now()))
            )
        }

        val myCalendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            myCalendar.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day)
                dateTextInputEditText.setText(SimpleDateFormat("MMM d, yyyy", Locale.US).format(myCalendar.time))
            }
        }

        dateTextInputEditText.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                dateSetListener,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}

/**
 * Convenience extension used for TextInputEditText for personal record to default to zero if there
 * is an empty string, which allows us to avoid a crash where string was empty!
 */
fun TextInputEditText.toFloatOrZero(): Float {
    val text = this.text.toString()
    return if (text.isEmpty()) {
        0f
    } else {
        text.toFloat()
    }
}
