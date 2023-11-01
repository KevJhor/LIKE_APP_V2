package com.example.like_app.ui.servicios

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.adapter.RestauranteAdapter
import com.example.like_app.model.RestauranteModel
import com.example.like_app.ui.adapter.estab_adapter
import com.example.like_app.ui.adapter.rest_adapter
import com.example.like_app.ui.model.model_estab
import com.example.like_app.ui.model.model_rest
import com.google.firebase.firestore.FirebaseFirestore


class ListaRestaurantes : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_lista_restaurantes, container, false)
        val db = FirebaseFirestore.getInstance()

        var lstRest: List<RestauranteModel>
        val rvRest: RecyclerView = view.findViewById(R.id.rc_rest)


        db.collection("Restaurante")
            .addSnapshotListener { snap, e ->
                if (e != null) {
                    Log.i("ERROR", "Ocurrio un Error")
                    return@addSnapshotListener
                }

                lstRest = snap!!.documents.map { document ->
                    RestauranteModel(
                        document["nombre"].toString(),
                        document["tiempo"].toString(),
                        document["precio_envio"].toString(),
                        document["imageUrl"].toString()

                    )
                }
                Log.i("Print", lstRest.size.toString())

                rvRest.adapter = RestauranteAdapter(lstRest)
                rvRest.layoutManager = LinearLayoutManager(requireContext())
            }

        return view
    }
    /*private fun ListRest(): List<model_rest>{
        val lstEstab: ArrayList<model_rest> = ArrayList()

        lstEstab.add(model_rest(1,R.drawable.productimabeef,"Restaurante","Esta es la descripcion del restaurante"))
        lstEstab.add(model_rest(2, R.drawable.imagen2kfc,"Restaurente","Esta es la descripcion del restaurante2"))
        lstEstab.add(model_rest(3, R.drawable.imgmenuejemplo,"Restaurente","Esta es la descripcion del restaurante3"))

        return lstEstab
    }*/


}

