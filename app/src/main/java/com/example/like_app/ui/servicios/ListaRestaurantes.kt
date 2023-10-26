package com.example.like_app.ui.servicios

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.ui.adapter.estab_adapter
import com.example.like_app.ui.adapter.rest_adapter
import com.example.like_app.ui.model.model_estab
import com.example.like_app.ui.model.model_rest




class ListaRestaurantes : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_lista_restaurantes, container, false)
        val rvEstab: RecyclerView = view.findViewById(R.id.rc_rest)
        view.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES

        rvEstab.layoutManager = LinearLayoutManager(requireContext())
        rvEstab.adapter = rest_adapter(ListRest())
        return view
    }
    private fun ListRest(): List<model_rest>{
        val lstEstab: ArrayList<model_rest> = ArrayList()

        lstEstab.add(model_rest(1,R.drawable.productimabeef,"Restaurante","Esta es la descripcion del restaurante"))
        lstEstab.add(model_rest(2, R.drawable.imagen2kfc,"Restaurente","Esta es la descripcion del restaurante2"))
        lstEstab.add(model_rest(3, R.drawable.imgmenuejemplo,"Restaurente","Esta es la descripcion del restaurante3"))

        return lstEstab
    }
}

