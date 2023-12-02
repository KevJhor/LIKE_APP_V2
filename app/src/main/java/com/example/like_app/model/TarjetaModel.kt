package com.example.like_app.model

import android.accounts.Account
import java.time.Month
import java.time.Year

data class TarjetaModel( val name: String,
                        val email:String,
                        val cardAccount: String,
                        val expMonth: String,
                        val expYear: String,
                        val cvv: String,
                        val saldo: Double)
