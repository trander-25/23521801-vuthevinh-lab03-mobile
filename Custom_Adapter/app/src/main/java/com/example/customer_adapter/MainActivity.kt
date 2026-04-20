package com.example.customer_adapter

import android.os.Bundle
import android.widget.ListView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lvUsers = findViewById<ListView>(R.id.lvUsers)

        val users = arrayListOf(
            User(name = "Vinh", hometown = "Hồ Chí Minh"),
            User(name = "Minh", hometown = "Hà Nội"),
            User(name = "Sơn", hometown = "Cao Bằng")
        )

        val adapter = UsersAdapter(this, users)
        lvUsers.adapter = adapter
    }
}