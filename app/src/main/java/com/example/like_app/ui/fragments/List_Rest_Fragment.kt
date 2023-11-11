package com.example.like_app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.ui.adapter.estab_adapter
import com.example.like_app.ui.model.model_estab

/**
 * A simple [Fragment] subclass.
 * Use the [List_Rest_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class List_Rest_Fragment : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View =  inflater.inflate(R.layout.fragment_list__rest_, container, false)
        val rvEstab: RecyclerView = view.findViewById(R.id.frag_estab)

        rvEstab.layoutManager = LinearLayoutManager(requireContext())
        rvEstab.adapter = estab_adapter(ListEstablecimiento())
        return view
    }


   private fun ListEstablecimiento(): List<model_estab>{
        val lstEstab: ArrayList<model_estab> = ArrayList()

        lstEstab.add(model_estab(1,R.drawable.tambo,"TAMBO","Market"))
        lstEstab.add(model_estab(2,R.drawable.imagen2kfc,"KFC","Restaurante"))


        return lstEstab
    }
}