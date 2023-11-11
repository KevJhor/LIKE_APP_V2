package com.example.like_app.ui.Gestion

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.like_app.R
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class GestionMenus : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View =inflater.inflate(R.layout.fragment_gestion_menu, container, false)
        val spnCategorias:Spinner= view.findViewById(R.id.spnCategorias)
        val spnCategoriasToMenus:Spinner=view.findViewById(R.id.spinnerCategoriasMenus)

        val db=FirebaseFirestore.getInstance()
        val brand_name="KFC";
        val docRef = db.collection("menu").document(brand_name)


        docRef.addSnapshotListener { snap, e ->
            if (e != null) {
                Log.e("TAG", "Error al obtener el documento: $e")
                return@addSnapshotListener
            }

            if (snap != null && snap.exists()) {
                // Acceder a los datos del documento
                val datos= snap.data
                if (datos is Map<*, *>) {

                    val claves=datos.keys.toList()
                    val adapterCate = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, claves)
                    adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spnCategorias.adapter = adapterCate
                    spnCategoriasToMenus.adapter = adapterCate

                    Log.i("claves", "Claves del mapa: $claves")
                }

                // Hacer algo con los datos, por ejemplo, imprimirlos
                Log.i("msg_rest", "Datos del documento en tiempo real: $datos")
            } else {
                Log.i("msg_rest", "El documento no existe o está vacío.")
            }
        }

        // BLOQUE DE CODIGO PARA LLENAR SPINNER CATEGORIA:







        return view
    }

}