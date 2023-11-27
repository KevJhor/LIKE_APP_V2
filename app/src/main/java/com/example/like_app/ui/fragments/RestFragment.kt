package com.example.like_app.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.adapter.ItemMenuAdapter
import com.example.like_app.adapter.RestauranteAdapter
import com.example.like_app.databinding.FragmentHomeBinding
import com.example.like_app.databinding.FragmentListaRestaurantesBinding
import com.example.like_app.databinding.FragmentRestBinding
import com.example.like_app.model.DatosEmpresaModel
import com.example.like_app.model.ItemMenu
import com.example.like_app.model.MenuModel
import com.example.like_app.model.RestauranteModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso


class RestFragment : Fragment(), ItemMenuAdapter.RecyclerViewEvent  {
    private var _binding: FragmentRestBinding? = null
    private lateinit var navController: NavController
    private lateinit var currentUID :  String
    private lateinit var orderUID :  String
    private  lateinit  var brand_name:String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var lstItemsMenu: ArrayList<ItemMenu> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentRestBinding.inflate(inflater, container, false)
        val view: View = binding.root
       // val view:View=inflater.inflate(R.layout.fragment_rest, container, false)
        val arguments = requireArguments()
        brand_name= arguments.getString("clave_nombre_rest").toString()


        val ivLogo:ImageView= view.findViewById(R.id.ivLogoRest)
        val ivPortada:ImageView=view.findViewById(R.id.ivPortadaRest)
        val tvNombreRest:TextView=view.findViewById(R.id.tvNombreRest)
        val tvHorario:TextView=view.findViewById(R.id.tvScheduleRest)
        val tvDireccion:TextView=view.findViewById(R.id.tvAddressRest)
        //LISTAS PARA MIS RecyclerViews
        val listCategorias:ArrayList<MenuModel> = ArrayList()
        //var lstItemsMenu: ArrayList<ItemMenu> = ArrayList()
        val auth = FirebaseAuth.getInstance()
        val db= FirebaseFirestore.getInstance()

        //LLENAR DATOS DE RESTAURANTE
        db.collection("datos_empresa")
            .whereEqualTo("brand_name",brand_name)
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
                    llenaDatos(datosRest,ivLogo,ivPortada,tvNombreRest,tvHorario,tvDireccion)
                    //Log.i("TAG", "Documento encontrado con éxito: $document")
                }
            }.addOnFailureListener { exception ->
                // Manejar errores
                // Log.i("TAG", "Error al buscar el documento", exception)
            }
        //LLENAR RV HORIZONTAL Y RV VERTICAL DE MENUS

        listCategorias.clear()
        lstItemsMenu.clear()

        if (brand_name != null) {
            db.collection("menu").document(brand_name).addSnapshotListener { snap, e ->
                if (e != null) {
                    Log.e("TAG", "Error al obtener el documento: $e")
                    return@addSnapshotListener
                }
                if (snap != null && snap.exists()) {
                    // Acceder a los datos del documento

                    val datos = snap.data
                    if (datos != null) {
                        // Iterar sobre las secciones (Sandwiches, Hamburguesas, Almuerzos, etc.)
                        for ((seccion,platosMap) in datos) {
                            //SE LLENA CADA CATEGORIA QUE TENGO EN MI COLECICON
                            listCategorias.add(MenuModel(1,seccion))
                            // Iterar sobre los platos dentro de cada sección
                            for ((platoNombre, platoData) in (platosMap as? Map<String, Any>
                                ?: emptyMap())) {
                                val platoModel = ItemMenu(
                                    imageUrl = (platoData as? Map<String, Any>
                                        ?: emptyMap())["img_url"]?.toString() ?: "",
                                    title = platoNombre,
                                    price = (platoData as? Map<String, Any>
                                        ?: emptyMap())["precio"]?.toString() ?: "",
                                    info = (platoData as? Map<String, Any>
                                        ?: emptyMap())["descripcion"]?.toString() ?: "")
                                lstItemsMenu.add(platoModel)
                            }

                        }
                        //SE LLENA LA BARRAR HORIZONTAL DE CATEGORIAS
                        val rvbtnmenus: RecyclerView =view.findViewById(R.id.rvBtnMenusRest)
                        rvbtnmenus.layoutManager=
                            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        rvbtnmenus.adapter=com.example.like_app.adapter.MenuAdapter(listCategorias)

                        //SE LLENA EL RECYCLER VIEW DE MENUS
                        val rvItems: RecyclerView =view.findViewById(R.id.rvItemRest)
                        rvItems.layoutManager= LinearLayoutManager(requireContext())
                        rvItems.adapter= ItemMenuAdapter(lstItemsMenu,this)

                        Log.i("msg_rest", "Datos del documento en tiempo real: $datos")
                    } else {
                        Log.i("msg_rest", "El documento no existe o está vacío.")
                    }

                }

            }
        }
        return view



    }

    //CREAR UNA ORDEN

    private fun llenaDatos(datos_rest: DatosEmpresaModel, ivLogo: ImageView,
                           ivPortada: ImageView, tvNombre:TextView, tvHorario:TextView, tvDireccion:TextView){
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

        //CREAR UNA ORDEN AL HACER CLICK A UN ITEM
        val item=lstItemsMenu[position]
        Toast.makeText(
            context,
            item.title,
            Toast.LENGTH_SHORT
        ).show()
        val bundle = Bundle()
        bundle.putString("clave_nombre_rest", brand_name)
        bundle.putString("clave_nombre_item", item.title)

        findNavController().navigate(R.id.action_restFragment_to_detalleItemFragment, bundle)
    }

}