package com.example.like_app.ui.servicios

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.adapter.RestauranteAdapter
import com.example.like_app.databinding.FragmentListaRestaurantesBinding
import com.example.like_app.model.RestauranteModel
import com.google.firebase.firestore.FirebaseFirestore


class ListaRestaurantes : Fragment() , RestauranteAdapter.RecyclerViewEvent {
    private lateinit var navController: NavController
    private var _binding: FragmentListaRestaurantesBinding? = null
    private val binding get() = _binding!!
    var lstRest: List<RestauranteModel> =emptyList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentListaRestaurantesBinding.inflate(inflater, container, false)
        val view: View = binding.root
        //val view: View = inflater.inflate(R.layout.fragment_lista_restaurantes, container, false)
        //CONEXION A FIRE BASE
        val db = FirebaseFirestore.getInstance()
        val rvRest: RecyclerView = view.findViewById(R.id.rc_rest)

        db.collection("Restaurante")
            .addSnapshotListener { snap, e ->
                if (e != null) {
                    Log.i("ERROR", "Ocurrio un Error")
                    return@addSnapshotListener
                }
                //LLENO lstREST
                lstRest = snap!!.documents.map { document ->
                    RestauranteModel(
                        document["brand_name"].toString(),
                        document["schedule"].toString(),
                        document["imageUrl"].toString()

                    )
                }
                Log.i("Print", lstRest.size.toString())
                rvRest.adapter = RestauranteAdapter(lstRest, this)
                rvRest.layoutManager = LinearLayoutManager(requireContext())

            }


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    override fun onItemClick(position: Int) {
        val rest=lstRest[position]
        Toast.makeText(
            context,
            rest.nombre,
            Toast.LENGTH_SHORT
        ).show()
        val bundle = Bundle()
        bundle.putString("clave", rest.nombre)
        findNavController().navigate(R.id.action_listaRestaurantes_to_restFragment, bundle)

    }

}