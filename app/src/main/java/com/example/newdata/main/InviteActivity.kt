package com.example.newdata.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import androidx.room.Room
import com.example.newdata.AppDatabase
import com.example.newdata.home.HomeActivity
import com.example.newdata.R
import com.example.newdata.User
import com.example.newdata.databinding.ActivityInviteBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InviteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInviteBinding
    private lateinit var appDb: AppDatabase
    private var user = Array(20){""}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInviteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userList.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        appDb = AppDatabase.getDatabase(this)

        val myUserName = intent.getStringExtra("passUsername")

        binding.inviteUsername.text = myUserName

        val userNames = intent.getStringArrayExtra("passData")

        if(userNames!=null) {
            for (i in 0..userNames.size-1) {
                user[i] = userNames[i]
            }
        }

        val userAdapter : ArrayAdapter<String> = ArrayAdapter(
            this,android.R.layout.simple_list_item_multiple_choice,
            user
        )

        binding.userList.adapter = userAdapter

        val userArray = Array(user.size){""}
        var counter = 0;


        binding.userList.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedItem: String = user[position]
            userArray[counter] = selectedItem
            counter++
            if(selectedItem.isEmpty()){
                counter = 0
            }

            /*val intent = Intent(this@InviteActivity, HomeActivity::class.java).also {
                it.putExtra("HomeUsername", myUserName.toString())
                it.putExtra("HomeID", myId.toString())
                it.putExtra("ListViewUser", selectedItem)
                startActivity(it)
            }*/
        })

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                if(user.contains(query)){
                    userAdapter.filter.filter(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                userAdapter.filter.filter(newText)
                return false
            }
        })

        binding.bottomNavigationView.setSelectedItemId(R.id.myInvite)

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.myHome -> {
                    val Intent = Intent(this, HomeActivity::class.java).also{
                        it.putExtra("passUsername",myUserName.toString())
                        it.putExtra("ListViewUser", userArray)
                        it.putExtra("Counter", counter)
                        it.putExtra("passData", user)
                        startActivity(it)
                        finish()
                    }
                    true
                }
                R.id.myInvite -> {

                    true
                }
                R.id.mySettings -> {
                    val Intent = Intent(this, SettingsActivity::class.java).also{
                        it.putExtra("passUsername",myUserName.toString())
                        it.putExtra("passData", user)
                        startActivity(it)
                        finish()
                    }
                    true
                }
                else -> false
            }
        }
    }
}