package com.example.like_app.ui.Gestion

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.like_app.R
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore




class GestionMenus : Fragment() {
    private  lateinit  var brand_name:String
    var misCategorias:List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,

        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View =inflater.inflate(R.layout.fragment_gestion_menu, container, false)

        val spnCategorias:Spinner= view.findViewById(R.id.spnCategorias)
        val spnCategoriasToMenus:Spinner=view.findViewById(R.id.spinnerCategoriasMenus)
        val etCategorias:EditText= view.findViewById(R.id.etCategoria)

        val etNomPlato:EditText=view.findViewById(R.id.etNomPLato)
        val etPrecioPlato:EditText=view.findViewById(R.id.etPrecioPlato)
        val etDescripPlato:EditText=view.findViewById(R.id.etDescripPlato)
        val etImgPlato:EditText=view.findViewById(R.id.etImgPlato)

        //BOTONES
        val btnCrearCat: Button = view.findViewById(R.id.btnCrearCat)
        val btnModifCat: Button = view.findViewById(R.id.btnModifCat)
        val btnAgregarPlato:Button=view.findViewById(R.id.btnAgregarPlato)
        //PARA FIRBEASE
        val db=FirebaseFirestore.getInstance()


        val arguments = requireArguments()
        brand_name= arguments.getString("clave_nombre_rest").toString()

        val docRef = db.collection("menu").document(brand_name)
        //VARIABLES GLOBALES:
        var categoria:String=""
        var categoriaPlato:String=""
        //CODIGO PARA OBTENER LAS CATEGORIAS
        docRef.addSnapshotListener { snap, e ->
            if (e != null) {
                Log.e("TAG", "Error al obtener el documento: $e")
                return@addSnapshotListener
            }

            if (snap != null && snap.exists()) {
                // Acceder a los datos del documento
                val datos= snap.data

                if (datos is Map<*, *>) {

                    var claves:List<String> = listOf(" ")
                   claves= claves +datos.keys.toList()

                    misCategorias=claves
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

        // BLOQUE DE CODIGO PARA CAPTURAR CUANDO EL USUARIO SELECCIONA UN ELEMENTO DE MI SPINNER

        spnCategorias.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (misCategorias != null && p2 in misCategorias.indices) {
                    categoria= misCategorias[p2]

                } else {
                    // Manejar el caso cuando misClaves es nulo o p2 está fuera de los límites de la lista
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
        spnCategoriasToMenus.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (misCategorias != null && p2 in misCategorias.indices) {
                    categoriaPlato=misCategorias[p2]

                } else {
                    // Manejar el caso cuando misClaves es nulo o p2 está fuera de los límites de la lista
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
        //FUNCION PARA CREAR UNA CATEGORIA
        btnCrearCat.setOnClickListener {

                val newCategory=etCategorias.text.toString()
                docRef.update(newCategory, hashMapOf<String, Any>())
                    .addOnSuccessListener {
                        etCategorias.setText("")
                        mostrarMsj(requireContext(),"Catregoria agregada con exito")
                        Log.i("tag","Seccion agregada con exito")
                    }
                    .addOnFailureListener { e ->

                        Log.i("tag", "Error al agregar sección", e)


                    }



        }

        //FUNCION PARA MODIFICAR UNA CATEGORIA

        btnModifCat.setOnClickListener {
            if(categoria!="") {
                val category = categoria
                val renameCategory = etCategorias.text.toString()
                docRef.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // Obtiene el Map actual
                        val miCampoMap =
                            documentSnapshot[category] as? MutableMap<String, Any> ?: mutableMapOf()
                        // Actualiza el documento con el nuevo Map
                        docRef.update(renameCategory, miCampoMap)
                            .addOnSuccessListener {
                                // Maneja el éxito, por ejemplo, muestra un mensaje de éxito
                                etCategorias.setText("")
                                Log.i("tag", "Campo de tipo Map actualizado con éxito")
                            }
                            .addOnFailureListener { e ->
                                // Maneja el error, por ejemplo, muestra un mensaje de error
                                Log.i("tag", "Error al actualizar el campo de tipo Map", e)
                            }

                        docRef.update(category, FieldValue.delete())
                            .addOnSuccessListener {
                                Log.d("tag", "Campo original eliminado con éxito")
                            }
                            .addOnFailureListener { e ->
                                Log.w("tag", "Error al eliminar campo original", e)
                            }

                    }
                }

            }

        }


        //FUNCION PARA AGREGAR UN PLATO
        btnAgregarPlato.setOnClickListener{
            val nomPLato=etNomPlato.text.toString()
            val descripPlato=etDescripPlato.text.toString()
            val imgPlato=etImgPlato.text.toString()
            val precioPlato=etPrecioPlato.text.toString()

            val plato = hashMapOf(
                "descripcion" to descripPlato,
                "img_url" to imgPlato,
                "precio" to precioPlato
            )

            docRef.get().addOnSuccessListener {snap->
                if(snap.exists()){
                    val datos=snap.data
                    Log.i("categoria",categoriaPlato)
                    if(!categoriaPlato.isEmpty()){
                        val mapCategoria= datos?.get(categoriaPlato) as? MutableMap<String, Any> ?: mutableMapOf()
                        //SE CREA UN NUEVO MAP DENTRO DE ESA CATEGORIA
                        mapCategoria[nomPLato] = plato

                        // Actualiza el documento en Firestore con el nuevo mapa de datos
                        docRef.update(categoriaPlato, mapCategoria)
                            .addOnSuccessListener {
                                // Maneja el éxito, por ejemplo, muestra un mensaje de éxito
                                mostrarMsj(requireContext(),"Nuevo Plato agregado con exito")
                                Log.d("tag", "Nuevo plato agregado con éxito")
                            }
                            .addOnFailureListener { e ->
                                // Maneja el error, por ejemplo, muestra un mensaje de error
                                Log.w("tag", "Error al agregar nuevo plato", e)
                            }
                    }

                }



            }



        }


        return view
    }

    fun mostrarMsj(context: Context, mensaje: String) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
    }

}