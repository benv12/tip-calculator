package com.example.newdata.home.bottomnavigationpages

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.newdata.R
import com.example.newdata.common.BaseFragment
import com.example.newdata.common.SnackBarMessage
import com.example.newdata.databinding.FragmentSettingsBinding
import com.example.newdata.home.MainViewModel
import com.example.newdata.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment: BaseFragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: MainViewModel by viewModels()

    private var isCurrentUserAdded: Boolean? = false

    override fun getRoot(): View? {
        return binding.root
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingsUserId.text = getString(R.string.user_id_title_input, viewModel.userId)
        binding.settingsUsername.text = getString(R.string.username_title_input, viewModel.userName)

        binding.actionLogout.setOnClickListener {
            viewModel.invalidateUserAndDataBase()
            // Start LoginActivity after performing logout
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)

            showSnackBar(SnackBarMessage(R.string.message_successful_logout))

            requireActivity().finish()
        }

    }

    override fun onResume() {
        super.onResume()
        isCurrentUserAdded = viewModel.isUserAdded

        if (!isCurrentUserAdded!!) {
            viewModel.userName?.let {
                viewModel.addGuest(it)
                viewModel.setUserAdded(true)
            }
        }
    }
}