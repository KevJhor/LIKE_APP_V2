package com.example.like_app.ui.payonline

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.adapter.VoucherAdapter
import com.example.like_app.model.VoucherModel
import com.google.firebase.firestore.FirebaseFirestore

class ChkdepoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View =  inflater.inflate(R.layout.fragment_chkdepo, container, false)

        val db = FirebaseFirestore.getInstance()
        var lstchkOrdenes: List<VoucherModel>
        val rvChkOrdenes: RecyclerView = view.findViewById(R.id.rvChkOrden)

        db.collection("Voucher")
            .addSnapshotListener { snap, e ->
                if(e != null){
                    Log.i("ERROR", "Ocurrio un error")
                    return@addSnapshotListener
                }
                lstchkOrdenes = snap!!.documents.map { document ->
                    VoucherModel(
                        document["ImageUrl"].toString()
                    )
                }
                rvChkOrdenes.adapter = VoucherAdapter(lstchkOrdenes)
                rvChkOrdenes.layoutManager = LinearLayoutManager(requireContext())
            }
        return view
    }
}