package com.example.newdata.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.newdata.home.HomeActivity
import com.example.newdata.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {

    private lateinit var settings_username : TextView
    private lateinit var tv_ID : TextView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settings_username = findViewById(R.id.settings_username)
        tv_ID = findViewById(R.id.tv_ID)
        btnLogout = findViewById(R.id.btnLogout)

        val myUserName = intent.getStringExtra("SettingUsername")
        val myId = intent.getStringExtra("SettingID")

        settings_username.text = "Username: " + myUserName
        tv_ID.text = "ID: " + myId

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setSelectedItemId(R.id.mySettings)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.myHome -> {
                    val Intent = Intent(this, HomeActivity::class.java).also{
                        it.putExtra("HomeUsername",myUserName.toString())
                        it.putExtra("HomeID",myId.toString())
                        startActivity(it)
                        finish()
                    }
                    //startActivity(Intent)
                    //finish()
                    true
                }
                R.id.myInvite -> {
                    val Intent = Intent(this, InviteActivity::class.java)
                    startActivity(Intent)
                    finish()
                    true
                }
                R.id.mySettings -> {

                    true
                }
                else -> false
            }
        }

        btnLogout.setOnClickListener{
            val Intent = Intent(this, MainActivity::class.java)
            startActivity(Intent)
        }
    }
}