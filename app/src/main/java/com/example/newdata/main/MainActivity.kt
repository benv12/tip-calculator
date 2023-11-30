package com.example.newdata.main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.room.Database
import com.example.newdata.AppDatabase
import com.example.newdata.home.HomeActivity
import com.example.newdata.User
import com.example.newdata.UserDao
import com.example.newdata.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var appDb : AppDatabase
    private var userArray = Array(20){""}

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDb = AppDatabase.getDatabase(this)

                binding.btnLogin.setEnabled(false)

                getUserNames()

                binding.myPassword.addTextChangedListener(object: TextWatcher{
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }
                    //text observer
                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {

                    }

                    override fun afterTextChanged(s: Editable?) {
                        if(binding.myPassword.text.isNotEmpty()){
                            binding.btnLogin.setEnabled(true)
                        }
                    }
                })

                binding.btnLogin.setOnClickListener {
                    val username = binding.myUsername.text.toString()

                    writeData()

                    val Intent = Intent(this, HomeActivity::class.java).also {
                        it.putExtra("passUsername", username)
                        //it.putExtra("ID", ID)
                        it.putExtra("passData", userArray)

                        startActivity(it)
                    }
                    // startActivity(Intent)
                }



        /*binding.btnEnter.setOnClickListener{

            readData()
        }*/

        binding.newDelete.setOnClickListener{
            GlobalScope.launch {
                appDb.userDao().deleteAll()
            }
        }
    }

    private fun writeData(){

        val userName = binding.myUsername.text.toString()
        val passWord = binding.myPassword.text.toString()

        if(userName.isNotEmpty() && passWord.isNotEmpty()){

            val user= User(
                null,userName,passWord,null
            )
            GlobalScope.launch(Dispatchers.IO){

                appDb.userDao().insert(user)

            }

            binding.myUsername.text.clear()
            binding.myPassword.text.clear()

            Toast.makeText(this@MainActivity,"Successfully written",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this@MainActivity,"Please Enter Data",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUserNames(){
        GlobalScope.launch {
            val temp: Array<String> = appDb.userDao().getUserNames()
            if(temp!=null) {
                userArray = temp

            }
        }
    }

    /*private suspend fun displayData(user: User){

        withContext(Dispatchers.Main){
            binding.usernameLabel.text = user.userName

        }
    }
    private fun readData(){

        val rollNo = binding.myRollnum.text.toString()

        if(rollNo.isNotEmpty()){
            lateinit var user: User

            GlobalScope.launch {

                user = appDb.userDao().findByRoll(rollNo.toInt())
                displayData(user)
            }
        }
    }*/
}