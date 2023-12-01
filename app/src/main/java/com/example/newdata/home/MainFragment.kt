package com.example.newdata.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.newdata.R
import com.example.newdata.Utils.ActivityUtils
import com.example.newdata.common.BaseFragment
import com.example.newdata.databinding.FragmentMainBinding
import com.example.newdata.home.bottomnavigationpages.HomeFragment
import com.example.newdata.home.bottomnavigationpages.InviteFragment
import com.example.newdata.home.bottomnavigationpages.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment() {
    private lateinit var binding: FragmentMainBinding

    override fun getRoot(): View? {
        return binding.root
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // By default, load HomeFragment first
        ActivityUtils.replaceFragment(
            childFragmentManager,
            HomeFragment(),
            R.id.subFragmentContainer
        )

        setupBottomNavView()
    }

    private fun setupBottomNavView() {
        val bottomNavView = binding.bottomNavView
        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.myHome -> {
                    ActivityUtils.replaceFragment(
                        childFragmentManager,
                        HomeFragment(),
                        R.id.subFragmentContainer
                    )
                    true
                }
                R.id.myInvite -> {
                    ActivityUtils.replaceFragment(
                        childFragmentManager,
                        InviteFragment(),
                        R.id.subFragmentContainer
                    )
                    true
                }
                R.id.mySettings -> {
                    ActivityUtils.replaceFragment(
                        childFragmentManager,
                        SettingsFragment(),
                        R.id.subFragmentContainer
                    )
                    true
                }
                else -> false
            }
        }
    }
}