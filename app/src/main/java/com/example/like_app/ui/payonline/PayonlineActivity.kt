package com.example.like_app.ui.payonline

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.like_app.R

class PayonlineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_online)

        val btndepcuenta: Button = findViewById(R.id.btndepcuenta)
        val btnadddebito: Button = findViewById(R.id.btndebito)
        val btnaddcredito: Button = findViewById(R.id.btncredito)

        btndepcuenta.setOnClickListener{
            val intent = Intent(this, DepositoActivity::class.java)
            startActivity(intent)
        }
        btnaddcredito.setOnClickListener {
            val intent = Intent(this, AddcardActivity::class.java)
            startActivity(intent)
        }
        btnadddebito.setOnClickListener {
            val intent = Intent(this, AddcardActivity::class.java)
            startActivity(intent)
        }


    }
}