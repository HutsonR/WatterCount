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
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.wattercount.DialogListener
import com.example.wattercount.R


class VariableDialogFragment(private val layoutResourceId: Int) : DialogFragment() {
    private val TAG = "debugTag"

    private var selectedButtonId: Int = 0
    private var editTextValue: String = ""
    private lateinit var selectedRadioButtonTag: String
    private lateinit var editText: TextView
    private lateinit var dialogListener: DialogListener

    //  проверка, что активити, вызывающая DialogFragment, реализует интерфейс DialogListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dialogListener = parentFragment as DialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("Parent fragment must implement DialogListener")
        }
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getDialog()!!.window?.setBackgroundDrawableResource(R.drawable.round_corner_20)
        val view = inflater.inflate(layoutResourceId, container, false)

        val button350 = view.findViewById<Button>(R.id.radio_350)
        val button500 = view.findViewById<Button>(R.id.radio_500)
        val button1000 = view.findViewById<Button>(R.id.radio_1000)

        val buttons = arrayOf(button350, button500, button1000)
        // Устанавливаем слушатели для кнопок
        for (button in buttons) {
            button.setOnClickListener {
                onButtonClicked(buttons, button)
            }
        }

        val saveButton = view.findViewById<Button>(R.id.saveAddButton)
        val cancelButton = view.findViewById<Button>(R.id.cancelAddButton)
        saveButton.setOnClickListener {
            handleSaveButtonClicked()
            dismiss()
        }
        cancelButton.setOnClickListener {
            dismiss()
        }

        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        // Устанавиваем слушатель на выбор кнопки
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            // нахождение выбранной кнопки
            view.findViewById<RadioButton>(checkedId)?.apply {
                selectedRadioButtonTag = tag as String
            }
        }

        editText = view.findViewById(R.id.customAddValue)
        // Устанавливаем слушатель для поля EditText
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editTextValue = s.toString()
                if (s != null) {
                    if (s.isNotEmpty()) {
                        selectedButtonId = 0
                        radioGroup.clearCheck()
                    }
                }
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

    private fun onButtonClicked(buttons: Array<Button>, clickedButton: Button) {
        selectedButtonId = clickedButton.id

        editTextValue = "" // Сбрасываем значение EditText при выборе кнопки
        editText.text = "" // Сбрасываем текст EditText при выборе кнопки

        updateSaveButtonState(clickedButton.rootView.findViewById(R.id.saveAddButton))
    }


    private fun updateSaveButtonState(saveButton: Button) {
        val isButtonSelected = selectedButtonId != 0
        val isEditTextFilled = editTextValue.isNotEmpty()

        saveButton.isEnabled = isButtonSelected || isEditTextFilled
    }


    private fun handleSaveButtonClicked() {
        if (selectedButtonId != 0)
            dialogListener.onConfirmAddDialogResult(selectedRadioButtonTag)
        else if (editTextValue.isNotEmpty()) {
            dialogListener.onConfirmAddDialogResult(editTextValue)
        }
    }
}