package com.example.newdata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import com.google.android.material.bottomnavigation.BottomNavigationView

class InviteActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var userList: ListView
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite)

        userList = findViewById(R.id.userList)
        searchView = findViewById(R.id.searchView)

        val user = arrayOf("Ben","Missy","Gio","Ginger","Pooty")

        val userAdapter : ArrayAdapter<String> = ArrayAdapter(
            this,android.R.layout.simple_list_item_1,
            user
        )

        userList.adapter = userAdapter

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
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













        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setSelectedItemId(R.id.myInvite)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.myHome -> {
                    val Intent = Intent(this,HomeActivity::class.java)
                    startActivity(Intent)
                    finish()
                    true
                }
                R.id.myInvite -> {

                    true
                }
                R.id.mySettings -> {
                    val Intent = Intent(this,SettingsActivity::class.java)
                    startActivity(Intent)
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}