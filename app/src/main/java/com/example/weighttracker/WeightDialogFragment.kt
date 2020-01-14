package com.example.weighttracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.dialog_weight_fragment.*

class WeightDialogFragment: DialogFragment() {

    private val weightDialogSharedViewModel by lazy {
        requireActivity().run {
            ViewModelProviders.of(this)[WeightDialogSharedViewModel::class.java]
        }
    }

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

        weightDialogToolbar.apply {
            title = "New Weight Entry"
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
        weightDialogSharedViewModel.saveNewWeightEntry(
            WeightEntry(
                dateTextInputEditText.text.toString(),
                weightTextInputEditText.toFloatOrZero()
            )
        )
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
