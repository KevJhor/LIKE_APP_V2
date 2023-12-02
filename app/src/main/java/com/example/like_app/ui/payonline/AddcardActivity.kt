package com.example.like_app.ui.payonline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.like_app.R
import com.example.like_app.adapter.RestauranteAdapter
import com.example.like_app.model.RestauranteModel
import com.example.like_app.model.TarjetaModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class AddcardActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    var lstTarj: List<TarjetaModel> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addcard)


        var etAccount: EditText = findViewById(R.id.etAccount)
        var etExpMonth: EditText = findViewById(R.id.etExpirationM)
        var etExpYear: EditText = findViewById(R.id.etExpirationY)
        var btnGuardar: Button = findViewById(R.id.btnaddtarjeta)

        btnGuardar.setOnClickListener {
            val user: FirebaseUser? = auth.currentUser
            val uid = user?.uid

            val account = etAccount.text.toString()
            val expM = etExpMonth.text.toString()
            val expY = etExpYear.text.toString()

            /*db.collection("tarjeta")
                .addSnapshotListener { snap, e ->
                    if (e != null) {
                        Log.i("ERROR", "Ocurrio un Error")
                        return@addSnapshotListener
                    }
                    lstTarj = snap!!.documents.map { document ->
                        TarjetaModel(
                            document["name"].toString(),
                            document["email"].toString(),
                            document["cardAccount"].toString(),
                            document["expMonth"].toString(),
                            document["expYear"].toString(),
                            document["cvv"].toString(),
                            document["saldo"].toString()
                        )
                    }
                }

*/
        }

    }
}