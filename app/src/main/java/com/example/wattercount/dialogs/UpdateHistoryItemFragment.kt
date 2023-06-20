package com.example.wattercount.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.wattercount.R
import com.example.wattercount.UpdateHistoryItemListener

class UpdateHistoryItemFragment(private val layoutResourceId: Int) : DialogFragment() {
    private val TAG = "debugTag"

    private var editTextValue: String = ""
    private lateinit var editText: TextView
    private lateinit var dialogListener: UpdateHistoryItemListener

    //  проверка, что активити, вызывающая DialogFragment, реализует интерфейс DialogListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dialogListener = context as UpdateHistoryItemListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement DialogListener")
        }
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getDialog()!!.window?.setBackgroundDrawableResource(R.drawable.round_corner_20)
        val view = inflater.inflate(layoutResourceId, container, false)

        val saveButton = view.findViewById<Button>(R.id.saveChangeButton)
        val cancelButton = view.findViewById<Button>(R.id.cancelChangeButton)
        saveButton.setOnClickListener {
            handleSaveButtonClicked()
            dismiss()
        }
        cancelButton.setOnClickListener {
            dismiss()
        }

        editText = view.findViewById(R.id.customAddChangeValue)
        // Устанавливаем слушатель для поля EditText
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editTextValue = s.toString()
                updateSaveButtonState(saveButton)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Изначально деактивируем кнопку "Сохранить"
        updateSaveButtonState(saveButton)

        return view
    }


    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }


    private fun updateSaveButtonState(saveButton: Button) {
        val isEditTextFilled = editTextValue.isNotEmpty()

        saveButton.isEnabled = isEditTextFilled
    }


    private fun handleSaveButtonClicked() {
        if (editTextValue.isNotEmpty()) {
            dialogListener.onUpdateResult(editTextValue)
        }
    }
}