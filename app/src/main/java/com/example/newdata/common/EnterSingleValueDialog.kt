package com.example.newdata.common

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.newdata.databinding.FragmentEnterSingleValueDialogBinding

class EnterSingleValueDialog : NoTitleDialogFragment() {
    private var _positiveListener: OnConfirmListener? = null
    private lateinit var _binding: FragmentEnterSingleValueDialogBinding
    private var _watcher: TextWatcher? = null
    private lateinit var _inputString: String

    companion object {
        private const val ARGS = "args"
        private const val TITLE = 0
        private const val HINT = 1
        private const val POSITIVE_ACTION = 2
    }

    class Builder {
        private var title: String? = null
        private var hint: String? = null
        private var positiveLabel: String? = null
        private var positiveListener: OnConfirmListener? = null
        private var cancellable = false

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setHint(hint: String): Builder {
            this.hint = hint
            return this
        }

        fun setPositiveAction(positiveLabel: String, listener: OnConfirmListener): Builder {
            this.positiveLabel = positiveLabel
            this.positiveListener = listener
            return this
        }

        fun setCancellable(cancellable: Boolean): Builder {
            this.cancellable = cancellable
            return this
        }

        fun create(): EnterSingleValueDialog {
            val dialog = EnterSingleValueDialog()
            positiveListener?.let { dialog.setPositiveListener(it) }
            dialog.isCancelable = cancellable

            val arguments = arrayOf(title, hint, positiveLabel)
            val args = Bundle()
            args.putStringArray(ARGS, arguments)
            dialog.arguments = args

            return dialog

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEnterSingleValueDialogBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        _binding.actionEnter.isEnabled = false

        _watcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                checkButtonEnabled()
            }
        }

        _binding.edit.addTextChangedListener(_watcher)


        arguments?.getStringArray(ARGS)?.let { arguments ->
            _binding.title.text = arguments[TITLE]
            _binding.valueTextInputLayout.hint = arguments[HINT]

            val positiveLabel = arguments[POSITIVE_ACTION]

            _binding.actionEnter.text = positiveLabel
            _binding.actionEnter.setOnClickListener {
                _positiveListener?.onConfirm(_inputString)
                dismiss()
            }

            _binding.actionCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun checkButtonEnabled() {
        if (_binding.edit.text.toString().isNotEmpty()) {
            _binding.actionEnter.isEnabled = true
            _inputString = _binding.edit.text.toString()
        } else {
            _binding.actionEnter.isEnabled = false
        }
    }

    fun setPositiveListener(positiveListener: OnConfirmListener) {
        this._positiveListener = positiveListener
    }

    interface OnConfirmListener {
        fun onConfirm(text: String)
    }

}