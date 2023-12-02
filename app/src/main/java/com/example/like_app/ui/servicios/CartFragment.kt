package com.example.like_app.ui.servicios

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.adapter.CartAdapter
import com.example.like_app.model.CartModel
import com.example.like_app.model.ItemMenu
import com.example.like_app.ui.payonline.DepositoFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class CartFragment : Fragment() ,CartAdapter.CartAdapterListener{

    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: CartAdapter
    private lateinit var cartList: ArrayList<CartModel>
    private var orderDatabaseReference = Firebase.firestore.collection("orders")
    private var subTotalPrice:Double = 0.00
    private var totalPrice:Double = 0.00
    lateinit var tvTotalCart:TextView
    lateinit var tvSubTotalCart:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_cart, container, false)
        val rvCartItems:RecyclerView=view.findViewById(R.id.rvCartItems)
        val btnPagar: Button = view.findViewById(R.id.btnPagar)
        tvTotalCart=view.findViewById(R.id.tvTotalCart)
        tvSubTotalCart=view.findViewById(R.id.tvSubTotalCart)
        auth=FirebaseAuth.getInstance()
        val layoutManager = LinearLayoutManager(context)
        cartList = ArrayList()
        retrieveCartItems(tvSubTotalCart,tvTotalCart)

        adapter = CartAdapter(cartList)
        adapter.cartAdapterListener = this
        rvCartItems.adapter = adapter
        rvCartItems.layoutManager = layoutManager

        btnPagar.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_payonlineFragment)

        }

        return view

    }
    /*
    private fun retrieveCartItems(tvSubTotalCart:TextView,tvTotalCart:TextView) {
        orderDatabaseReference
            .whereEqualTo("uid",auth.currentUser!!.uid)
            .whereEqualTo("estado", "Activo")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (item in querySnapshot) {
                    val cartProduct = item.toObject<CartModel>()
                    cartList.add(cartProduct)
                    //totalPrice += cartProduct.price.toDouble()*cartProduct.quantity
                    adapter.notifyDataSetChanged()
                    tvSubTotalCart.text=adapter.getMonto().toString()
                    /*
                    val itemData = item.data
                    val uid=itemData["uid"].toString()
                    val orderName=itemData["orderName"].toString()
                    val orderProveedor=itemData["orderProveedor"].toString()
                    val imgUrl=itemData["imageUrl"].toString()
                    //val cartProduct = item.toObject<CartModel>()
                    val cartProduct=CartModel(uid,orderProveedor,orderName,imgUrl,4,"5")
                    cartList.add(cartProduct)
                    Log.i("CartFragment",orderName)*/
                }

            }
            .addOnFailureListener{
                Log.i("CartFragment","EROR AL REALIZAR EL RETRIEVE CART ITEMS")
            }
    }

*/
    private  fun retrieveCartItems(tvSubTotalCart:TextView,tvTotalCart:TextView){
        orderDatabaseReference
            .whereEqualTo("uid", auth.currentUser!!.uid)
            .whereEqualTo("estado", "Activo")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    // Filtrar solo los objetos que son instancias de Map
                    val mapData = document.data?.filterValues { it is Map<*, *> } as? Map<String, Map<String, Any>>
                    // Aquí mapData contiene solo los objetos mapas
                    if (mapData != null) {
                        for ((key, mapItem) in mapData) {
                           val orderName= (mapItem as? Map<String, Any>?: emptyMap())["orderName"]?.toString() ?: ""
                            val imageUrl= (mapItem as? Map<String, Any>?: emptyMap())["imageUrl"]?.toString() ?: ""
                            val quantity= (mapItem as? Map<String, Any>?: emptyMap())["quantity"]?.toString() ?: ""
                            val price= (mapItem as? Map<String, Any>?: emptyMap())["price"]?.toString() ?: ""
                            val cartProduct=CartModel(orderName,imageUrl, quantity.toInt(),price)
                            cartList.add(cartProduct)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
                tvSubTotalCart.text=adapter.getMonto().toString()
                // Ahora cartList contiene los objetos CartModel creados a partir de los mapas de datos
                // Puedes realizar operaciones adicionales aquí según tus necesidades
            }
            .addOnFailureListener{
                Log.i("CartFragment","ERROR AL REALIZAR EL RETRIEVE CART ITEMS")
            }
    }
    override fun onCartTotalChanged(amountItems: Double) {
        tvSubTotalCart.text=amountItems.toString()
    }



}