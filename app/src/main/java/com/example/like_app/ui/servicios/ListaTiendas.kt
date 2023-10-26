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
import com.example.like_app.ui.model.model_estab

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListaTiendas.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListaTiendas : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View =  inflater.inflate(R.layout.fragment_lista_tiendas, container, false)
        val rvEstab: RecyclerView = view.findViewById(R.id.rc_tienda)


        rvEstab.layoutManager = LinearLayoutManager(requireContext())
        rvEstab.adapter = estab_adapter(ListEstablecimiento())
        return view
    }

    private fun ListEstablecimiento(): List<model_estab>{
        val lstEstab: ArrayList<model_estab> = ArrayList()

        lstEstab.add(model_estab(1,R.drawable.tambo,"TAMBO","Market"))
        lstEstab.add(model_estab(2, R.drawable.imgmifarma,"KFC","Restaurante"))


        return lstEstab
    }
}