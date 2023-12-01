package com.example.newdata.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.newdata.R
import com.example.newdata.Utils.ActivityUtils
import com.example.newdata.common.BaseFragment
import com.example.newdata.common.SnackBarMessage
import com.example.newdata.databinding.FragmentLoginBinding
import com.example.newdata.databinding.FragmentMainBinding
import com.example.newdata.home.MainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: BaseFragment() {
    private lateinit var _binding: FragmentLoginBinding

    private val _viewModel: LoginViewModel by viewModels()

    override fun getRoot(): View? {
        return _binding.root
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {// Set up TextWatcher for username EditText
        _binding.username.addTextChangedListener(createTextWatcher())

        // Set up TextWatcher for userPassword EditText
        _binding.userPassword.addTextChangedListener(createTextWatcher())

        // Set up TextWatcher for userId EditText
        _binding.userId.addTextChangedListener(createTextWatcher())

        _binding.actionClearUserDatabase.setOnClickListener {
            _viewModel.clearUserDatabase { success ->
                if (success) {
                    showSnackBar(SnackBarMessage(getString(R.string.message_successful_database_clear)))
                } else {
                    showSnackBar(SnackBarMessage(getString(R.string.error_database_clear)))
                }
            }
        }

        _binding.actionLogin.setOnClickListener {
            val username = _binding.username.text.toString()
            val userId = _binding.userId.text.toString()
            val password = _binding.userPassword.text.toString()

            _viewModel.isValidCredentials(username, password, userId) { isValid ->
                if (isValid) {
                    val fragment = MainFragment()
                    ActivityUtils.addFragmentWithBackStack(
                        parentFragmentManager,
                        fragment,
                        R.id.fragment_container,
                        null
                    )
                    showSnackBar(SnackBarMessage(getString(R.string.message_successful_login)))
                } else {
                    showSnackBar(SnackBarMessage(R.string.error_user_login))
                }
            }
        }

        _binding.actionClear.setOnClickListener {
            clearFields()
        }

    }

    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed in this case
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed in this case
            }

            override fun afterTextChanged(s: Editable?) {
                // Update button states when any text changes
                updateButtonState()
            }
        }
    }

    private fun updateButtonState() {
        val usernameEmpty = _binding.username.text.isNullOrEmpty()
        val passwordEmpty = _binding.userPassword.text.isNullOrEmpty()
        val userIdText = _binding.userId.text
        val userIdInvalidLength = userIdText.isNullOrEmpty() || userIdText.length != 7

        // Disable buttons if any of the fields are empty or invalid
        _binding.actionLogin.isEnabled = !(usernameEmpty || passwordEmpty || userIdInvalidLength)
        _binding.actionClear.isEnabled = !(usernameEmpty && passwordEmpty && userIdInvalidLength)
    }


    private fun clearFields() {
        // Clear EditText fields
        _binding.username.text?.clear()
        _binding.userPassword.text?.clear()
        _binding.userId.text?.clear()
    }
}