package com.example.newdata.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.newdata.R
import com.example.newdata.Utils.ActivityUtils
import com.example.newdata.common.BaseActivity
import com.example.newdata.home.bottomnavigationpages.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        val fragment = MainFragment()
        ActivityUtils.addFragment(supportFragmentManager, fragment, R.id.fragment_container)
    }
}