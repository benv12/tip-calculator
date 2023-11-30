package com.example.newdata.home

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.newdata.R
import com.example.newdata.databinding.ActivityHomeBinding
import com.example.newdata.main.InviteActivity
import com.example.newdata.main.SettingsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var split = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //gets the input from main activity
        val myUserName = intent.getStringExtra("passUsername")



        val passData = intent.getStringArrayExtra("passData")
        val temp = Array(20){""}
        if(passData!=null){
            for(i in 0..passData.size-1){
                temp[i]=passData[i]
            }
        }



        val usernames = Array(20){""}

        val InviteListView = intent.getStringArrayExtra("ListViewUser")
        val count = intent.getIntExtra("Counter", 0)
        split = count+1

        if(InviteListView!=null) {
            for (i in 0..InviteListView.size-1) {
                usernames[i] = InviteListView[i]
            }
        }


        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_list_item_1,
            usernames
        )
        binding.myListView.isVerticalScrollBarEnabled
        binding.myListView.adapter = arrayAdapter


        binding.tvUsername.text = myUserName.toString()
        //tv_username.text = myUserName.toString()

        binding.SeekBarPercent.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(ContentValues.TAG, "onProgressChanged $progress")
                binding.tipPercent.text = "$progress%"
                compute()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        binding.myBill.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(ContentValues.TAG, "afterTextChanged $s")
                compute()
            }
        })
        binding.btn15.setOnClickListener{
            binding.SeekBarPercent.progress = 15
        }
        binding.btn20.setOnClickListener{
            binding.SeekBarPercent.progress = 20
        }
        binding.btn25.setOnClickListener{
            binding.SeekBarPercent.progress = 25
        }
        binding.btn30.setOnClickListener{
            binding.SeekBarPercent.progress = 30
        }

        binding.bottomNavigationView.setSelectedItemId(R.id.myHome)

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.myHome -> {

                    true
                }
                R.id.myInvite -> {
                    val Intent = Intent(this, InviteActivity::class.java).also{

                        it.putExtra("passUsername", myUserName)
                        it.putExtra("passData", temp)
                        startActivity(it)
                        finish()
                    }

                    true
                }
                R.id.mySettings -> {

                    val Intent = Intent(this, SettingsActivity::class.java).also{

                        it.putExtra("passUsername", myUserName)
                        it.putExtra("passData", temp)
                        startActivity(it)
                        finish()
                    }

                    true
                }
                else -> false
            }
        }

    }
    private fun compute(){
        if(binding.myBill.text.isEmpty()){
            binding.tvTip.text = "$0.0"
            binding.tvBill.text = "$0.0"
            binding.tvSplit.text = "$0.0"
            return
        }
        val bill = binding.myBill.text.toString().toDouble()
        val tip = binding.SeekBarPercent.progress

        val tipAmount = bill * tip / 100
        val total = (bill + tipAmount)
        var splitBill = (bill + tipAmount)
        if(split!=0) {
            splitBill = (bill + tipAmount) / split
        }

        binding.tvTip.text = "$%.2f".format(tipAmount)
        binding.tvBill.text = "$%.2f".format(total)
        binding.tvSplit.text = "$%.2f".format(splitBill)
    }
}