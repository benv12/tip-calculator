package com.example.newdata.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.newdata.home.HomeActivity
import com.example.newdata.R
import com.example.newdata.databinding.ActivityInviteBinding
import com.example.newdata.databinding.ActivitySettingsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val myUserName = intent.getStringExtra("passUsername")


        val userNames = intent.getStringArrayExtra("passData")

        val user = Array(20){""}

        if(userNames!=null) {
            for (i in 0..userNames.size-1) {
                user[i] = userNames[i]
            }
        }

        binding.settingsUsername.text = "Username: " + myUserName

        binding.bottomNavigationView.setSelectedItemId(R.id.mySettings)

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.myHome -> {
                    val Intent = Intent(this, HomeActivity::class.java).also{
                        it.putExtra("passUsername",myUserName.toString())

                        it.putExtra("passData", user)
                        startActivity(it)
                        finish()
                    }

                    true
                }
                R.id.myInvite -> {
                    val Intent = Intent(this, InviteActivity::class.java).also{
                        it.putExtra("passUsername",myUserName.toString())

                        it.putExtra("passData", user)
                        startActivity(it)
                        finish()
                    }
                    true
                }
                R.id.mySettings -> {

                    true
                }
                else -> false
            }
        }

        binding.btnLogout.setOnClickListener{
            val Intent = Intent(this, MainActivity::class.java)
            startActivity(Intent)
        }
    }
}