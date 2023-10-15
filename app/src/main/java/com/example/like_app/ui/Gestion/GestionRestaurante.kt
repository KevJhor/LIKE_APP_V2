package com.example.like_app.ui.Gestion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.adapter.ItemMenuAdapter
import com.example.like_app.model.ItemMenu

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GestionRestaurante.newInstance] factory method to
 * create an instance of this fragment.
 */
class GestionRestaurante : Fragment() {
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
        val view:View = inflater.inflate(R.layout.fragment_gestion_restaurantes, container, false)
        val rvItems:RecyclerView=view.findViewById(R.id.rvItems)
        rvItems.layoutManager=LinearLayoutManager(requireContext())
        rvItems.adapter=ItemMenuAdapter(listItems())

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

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GestionRestaurante().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}