package com.example.like_app.ui.Gestion

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.menu.MenuAdapter
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.adapter.ItemMenuAdapter
import com.example.like_app.model.DatosEmpresaModel
import com.example.like_app.model.ItemMenu
import com.example.like_app.model.MenuModel
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso


class GestionRestaurante : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view:View = inflater.inflate(R.layout.fragment_gestion_restaurantes, container, false)
        val ivLogo:ImageView=view.findViewById(R.id.ivLogo)
        val ivPortada:ImageView=view.findViewById(R.id.ivPortada)
        val brand_name="KFC"
        val tvNombre:TextView=view.findViewById(R.id.tvBrandNameRest)
        val tvHorario:TextView=view.findViewById(R.id.tvHorarioRest)
        val tvDireccion:TextView=view.findViewById(R.id.tvDireccionRest)

        val btnMenus:Button=view.findViewById(R.id.btnMenus)

        val db=FirebaseFirestore.getInstance()
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
                    llenaDatos(datosRest,ivLogo,ivPortada,tvNombre,tvHorario,tvDireccion)



                    //Log.i("TAG", "Documento encontrado con éxito: $document")

                }

            }.addOnFailureListener { exception ->
                // Manejar errores
               // Log.i("TAG", "Error al buscar el documento", exception)
            }




        val rvItems:RecyclerView=view.findViewById(R.id.rvItems)
        rvItems.layoutManager=LinearLayoutManager(requireContext())
        rvItems.adapter=ItemMenuAdapter(listItems())

        val rvbtnmenus:RecyclerView=view.findViewById(R.id.rvBtnMenus)
        rvbtnmenus.layoutManager=LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvbtnmenus.adapter=com.example.like_app.adapter.MenuAdapter(listMenus())

        btnMenus.setOnClickListener{

            findNavController().navigate(R.id.action_gestionRestaurante_to_gestionMenus)

        }

        return view
    }
    private fun listItems():List<ItemMenu>{
        val listItems:ArrayList<ItemMenu> = ArrayList()
        listItems.add(ItemMenu(R.drawable.imgmenuejemplo,"Combo especial","Pizza mediana de especialidad, acompañamiento y bebida 250 ml ....",
            "S/. 35","30-40 min"))

        listItems.add(ItemMenu(R.drawable.imgmenuejemplo,"Titulo 1","Pizza mediana de especialidad, acompañamiento y bebida 250 ml ....",
            "S/. 20","15-20 min"))

        listItems.add(ItemMenu(R.drawable.imgmenuejemplo,"Titulo 2","Pizza mediana de especialidad, acompañamiento y bebida 250 ml ....",
            "S/. 15","40-50 min"))

        listItems.add(ItemMenu(R.drawable.kfc,"Titulo 3","Pizza mediana de especialidad, acompañamiento y bebida 250 ml ....",
            "S/. 40","20-40 min"))
        listItems.add(ItemMenu(R.drawable.imgmenuejemplo,"Titulo 4","Pizza mediana de especialidad, acompañamiento y bebida 250 ml ....",
            "S/. 10","25-30 min"))


        return listItems
    }

    private fun listMenus():List<MenuModel>{
        val listItems:ArrayList<MenuModel> = ArrayList()
        listItems.add(MenuModel(1,"Menu 1"))
        listItems.add(MenuModel(2,"Menu 2"))
        listItems.add(MenuModel(3,"Menu 3"))
        listItems.add(MenuModel(4,"Menu 4"))
        listItems.add(MenuModel(5,"Menu 5"))
        listItems.add(MenuModel(6,"Menu 6"))
        listItems.add(MenuModel(7,"Menu 7"))




        return listItems
    }

    private fun llenaDatos(datos_rest:DatosEmpresaModel,ivLogo:ImageView,
                           ivPortada:ImageView,tvNombre:TextView,tvHorario:TextView,tvDireccion:TextView){
        Picasso.get().load(datos_rest.img_logo_url).into(ivLogo)
        Picasso.get().load(datos_rest.img_portada_url).into(ivPortada)
        tvNombre.text=datos_rest.brand_name
        tvHorario.text=datos_rest.horario
        tvDireccion.text=datos_rest.direccion
    }

}