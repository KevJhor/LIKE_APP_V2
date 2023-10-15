package com.example.like_app.ui.servicios

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.adapter.ItemMenuAdapter
import com.example.like_app.adapter.RestauranteAdapter
import com.example.like_app.model.ItemMenu
import com.example.like_app.model.RestauranteModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListaRestaurantes.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListaRestaurantes : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_lista_restaurantes, container, false)
        val rvListRestaurante: RecyclerView =view.findViewById(R.id.rvRestaurante)
        rvListRestaurante.layoutManager= LinearLayoutManager(requireContext())
        rvListRestaurante.adapter= RestauranteAdapter(listRestaurantes())
        return view
    }






    private fun listRestaurantes():List<RestauranteModel>{
        val listItems:ArrayList<RestauranteModel> = ArrayList()
        listItems.add(RestauranteModel(1,"Citrico","25-30 min", "S/ 6", R.drawable.citrico))
        listItems.add(RestauranteModel(2,"KFC","25-30 min", "S/ 4", R.drawable.kfc))
        listItems.add(RestauranteModel(3,"Rest 3","25-30 min", "S/ 5", R.drawable.banner))
        listItems.add(RestauranteModel(4,"Rest 4","30-10", "S/ 7", R.drawable.banner))
        listItems.add(RestauranteModel(5,"Rest 5","15-20 min", "S/ 8", R.drawable.banner))





        return listItems
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListaRestaurantes.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListaRestaurantes().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}