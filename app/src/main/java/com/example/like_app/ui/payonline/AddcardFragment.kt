package com.example.like_app.ui.payonline

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.like_app.R
import com.example.like_app.model.TarjetaModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class AddcardFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    var lstTarj: List<TarjetaModel> = emptyList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_addcard, container, false)



        val etAccount: EditText = view.findViewById(R.id.etAccount)
        val etExpMonth: EditText = view.findViewById(R.id.etExpirationM)
        val etExpYear: EditText = view.findViewById(R.id.etExpirationY)
        val btnGuardar: Button = view.findViewById(R.id.btnaddtarjeta)

        btnGuardar.setOnClickListener {
            val user: FirebaseUser? = auth.currentUser
            val uid = user?.uid

            val account = etAccount.text.toString()
            val expM = etExpMonth.text.toString()
            val expY = etExpYear.text.toString()

            db.collection("tarjeta")
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

        }
        return view
    }


}
