package com.example.newdata.home

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.newdata.R
import com.example.newdata.main.InviteActivity
import com.example.newdata.main.SettingsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var myBill: EditText
    private lateinit var tipPercent: TextView
    private lateinit var SeekBarPercent: SeekBar
    private lateinit var tvTip: TextView
    private lateinit var tvBill: TextView
    private lateinit var btn15: Button
    private lateinit var btn20: Button
    private lateinit var btn25: Button
    private lateinit var btn30: Button
    private lateinit var tv_username: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        myBill = findViewById(R.id.myBill)
        tipPercent = findViewById(R.id.tipPercent)
        SeekBarPercent = findViewById(R.id.SeekBarPercent)
        tvTip = findViewById(R.id.tvTip)
        tvBill = findViewById(R.id.tvBill)
        btn15 = findViewById(R.id.btn15)
        btn20 = findViewById(R.id.btn20)
        btn25 = findViewById(R.id.btn25)
        btn30 = findViewById(R.id.btn30)
        tv_username = findViewById(R.id.tv_username)

        val myUserName = intent.getStringExtra("username")
        val myId = intent.getStringExtra("ID")
        val settingsUsername = intent.getStringExtra("HomeUsername")
        val setttingsID = intent.getStringExtra("HomeID")

        if(settingsUsername.equals(null)&&setttingsID.equals(null)){
            tv_username.text = myUserName.toString()+"#"+myId.toString()
        }
        if(myUserName.equals(null)&&myId.equals(null)){
            tv_username.text = settingsUsername.toString()+"#"+setttingsID.toString()
        }



        SeekBarPercent.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(ContentValues.TAG, "onProgressChanged $progress")
                tipPercent.text = "$progress%"
                compute()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        myBill.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(ContentValues.TAG, "afterTextChanged $s")
                compute()
            }
        })
        btn15.setOnClickListener{
            SeekBarPercent.progress = 15
        }
        btn20.setOnClickListener{
            SeekBarPercent.progress = 20
        }
        btn25.setOnClickListener{
            SeekBarPercent.progress = 25
        }
        btn30.setOnClickListener{
            SeekBarPercent.progress = 30
        }






        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setSelectedItemId(R.id.myHome)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.myHome -> {

                    true
                }
                R.id.myInvite -> {
                    val Intent = Intent(this, InviteActivity::class.java)
                    startActivity(Intent)
                    finish()
                    true
                }
                R.id.mySettings -> {
                    val Intent = Intent(this, SettingsActivity::class.java).also{
                        if(settingsUsername.equals(null)&&setttingsID.equals(null)){
                            it.putExtra("SettingUsername",myUserName.toString())
                            it.putExtra("SettingID",myId.toString())
                        }
                        if(myUserName.equals(null)&&myId.equals(null)){
                            it.putExtra("SettingUsername",settingsUsername.toString())
                            it.putExtra("SettingID",setttingsID.toString())
                        }

                        startActivity(it)
                        finish()
                    }
                    //startActivity(Intent)
                    //finish()
                    true
                }
                else -> false
            }
        }

    }
    private fun compute(){
        if(myBill.text.isEmpty()){
            tvTip.text = "$0.0"
            tvBill.text = "$0.0"
            return
        }
        val bill = myBill.text.toString().toDouble()
        val tip = SeekBarPercent.progress

        val tipAmount = bill * tip / 100
        val total = bill + tipAmount

        tvTip.text = "$%.2f".format(tipAmount)
        tvBill.text = "$%.2f".format(total)
    }
}