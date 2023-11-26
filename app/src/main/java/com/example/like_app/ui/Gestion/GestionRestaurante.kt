package com.example.like_app.ui.Gestion

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import android.widget.ImageView

import android.widget.TextView

import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.adapter.ItemMenuAdapter
import com.example.like_app.databinding.FragmentGestionRestaurantesBinding

import com.example.like_app.model.DatosEmpresaModel
import com.example.like_app.model.ItemMenu
import com.example.like_app.model.MenuModel
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import java.util.Currency
import java.util.Locale


class GestionRestaurante : Fragment(),ItemMenuAdapter.RecyclerViewEvent   {
    private var _binding: FragmentGestionRestaurantesBinding? = null
    private lateinit var navController: NavController
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentGestionRestaurantesBinding.inflate(inflater, container, false)
        val view: View = binding.root
        val ivLogo:ImageView=view.findViewById(R.id.ivLogo)
        val ivPortada:ImageView=view.findViewById(R.id.ivPortada)
        val brandName="KFC"
        val tvNombre:TextView=view.findViewById(R.id.tvBrandNameRest)
        val tvHorario:TextView=view.findViewById(R.id.tvHorarioRest)
        val tvDireccion:TextView=view.findViewById(R.id.tvDireccionRest)

        val btnMenus:Button=view.findViewById(R.id.btnMenus)
        //LISTAS PARA MIS RecyclerViews
        val listCategorias:ArrayList<MenuModel> = ArrayList()
        val lstItemsMenu: ArrayList<ItemMenu> = ArrayList()
        val db=FirebaseFirestore.getInstance()

        db.collection("datos_empresa")
            .whereEqualTo("brand_name",brandName)
            .get().addOnSuccessListener {snap->
                if(!snap.isEmpty){
                    val document=snap.documents[0]
                    val datosRest=DatosEmpresaModel(
                        document["brand_name"].toString(),
                        document["schedule"].toString(),
                        document["address"].toString(),
                        document["logo"].toString(),
                        document["portada"].toString()
                        )
                    //Log.i("TAG", "url imagen ${datosRest.img_logo_url}")
                    //tvNombre.text=datosRest.brand_name
                    llenaDatos(datosRest,ivLogo,ivPortada,tvNombre,tvHorario,tvDireccion)


                    Log.i("GESTION_REST", "Documento encontrado con éxito: $document")

                }

            }.addOnFailureListener { exception ->
                // Manejar errores
               Log.i("GESTION_REST", "Error al buscar el documento", exception)
            }


        db.collection("menu").document(brandName).addSnapshotListener { snap, e ->
            if (e != null) {
                Log.e("TAG", "Error al obtener el documento: $e")
                return@addSnapshotListener
            }
            if (snap != null && snap.exists()) {
                // Acceder a los datos del documento

                //SE LIMPIAN LAS LISTAS
                listCategorias.clear()
                lstItemsMenu.clear()

                val datos = snap.data
                if (datos != null) {
                    // Iterar sobre las secciones (Sandwiches, Hamburguesas, Almuerzos, etc.)
                    for ((seccion,platosMap) in datos) {
                        //SE LLENA CADA CATEGORIA QUE TENGO EN MI COLECICON
                        listCategorias.add(MenuModel(1,seccion))
                        // Iterar sobre los platos dentro de cada sección
                        for ((platoNombre, platoData) in (platosMap as? Map<String, Any>
                            ?: emptyMap())) {
                            val priceItem=(platoData as? Map<String, Any>
                                ?: emptyMap())["precio"]?.toString() ?: ""
                            val formatPrice=getMoneda(priceItem)
                            val platoModel = ItemMenu(
                                imageUrl = (platoData as? Map<String, Any>
                                    ?: emptyMap())["img_url"]?.toString() ?: "",
                                title = platoNombre,
                                price = formatPrice,
                                info = (platoData as? Map<String, Any>
                                    ?: emptyMap())["descripcion"]?.toString() ?: "")
                            lstItemsMenu.add(platoModel)
                        }

                    }
                    //SE LLENA LA BARRAR HORIZONTAL DE CATEGORIAS
                    val rvbtnmenus:RecyclerView=view.findViewById(R.id.rvBtnMenus)
                    rvbtnmenus.layoutManager=LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    rvbtnmenus.adapter=com.example.like_app.adapter.MenuAdapter(listCategorias)

                    //SE LLENA EL RECYCLER VIEW DE MENUS
                    val rvItems:RecyclerView=view.findViewById(R.id.rvItems)
                    rvItems.layoutManager=LinearLayoutManager(requireContext())
                    rvItems.adapter=ItemMenuAdapter(lstItemsMenu,this)

                    Log.i("msg_rest", "Datos del documento en tiempo real: $datos")
                } else {
                    Log.i("msg_rest", "El documento no existe o está vacío.")
                }

            }

        }
        btnMenus.setOnClickListener{

            findNavController().navigate(R.id.action_gestionRestaurante_to_gestionMenus)

        }
        return view
    }
    private fun llenaDatos(datos_rest:DatosEmpresaModel,ivLogo:ImageView,
                           ivPortada:ImageView,tvNombre:TextView,tvHorario:TextView,tvDireccion:TextView){
        Picasso.get().load(datos_rest.img_logo_url).into(ivLogo)
        Picasso.get().load(datos_rest.img_portada_url).into(ivPortada)
        tvNombre.text=datos_rest.brand_name
        tvHorario.text=datos_rest.horario
        tvDireccion.text=datos_rest.direccion
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onItemClick(position: Int) {

    }

    //UTILIDEADES
    private fun getMoneda(valor:String):String{
        //convertir a double
        val valorDouble=valor.toDouble()
        // Especifica la moneda que deseas usar
        val moneda= Currency.getInstance("PEN")
        // Crea un formato para la moneda y el idioma específicos
        val formatoMoneda = java.text.NumberFormat.getCurrencyInstance(Locale("es", "PE"))
        formatoMoneda.currency = moneda
        // Formatea el Double como una cantidad de dinero con el símbolo de la moneda
        val cantidadFormateada = formatoMoneda.format(valorDouble)
        return cantidadFormateada

    }

}