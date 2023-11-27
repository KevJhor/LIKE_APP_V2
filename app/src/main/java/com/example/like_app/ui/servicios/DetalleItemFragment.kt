package com.example.like_app.ui.servicios

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.like_app.R
import com.example.like_app.model.ItemMenu
import com.example.like_app.model.ProductOrderModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.util.Currency
import java.util.Locale
import java.util.UUID


class DetalleItemFragment : Fragment() {
    //VARIABLES GLOBALES
    private var cantidad:Int=1
    private val orderDatabaseReference = Firebase.firestore.collection("orders")
    private lateinit var currentUID :  String
    private lateinit var orderImageUrl:String
    private var orderQuantity:Int  = 1
    private lateinit var orderPrice:String
    private  lateinit var idOrder: String
    private  lateinit var brand_name: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_detalle_item, container, false)

        val arguments = requireArguments()
        val nameItem= arguments.getString("clave_nombre_item")
        brand_name=arguments.getString("clave_nombre_rest").toString()


        val ivItem:ImageView=view.findViewById(R.id.ivItemDetail)
        val tvName:TextView=view.findViewById(R.id.tvNameItemDetail)
        val description:TextView=view.findViewById(R.id.tvDescritionItemDetail)
        val price:TextView=view.findViewById(R.id.tvItemPriceDetail)
        val tvCantidad:TextView=view.findViewById(R.id.tvCantidadDetail)
        val tvSubTotal:TextView=view.findViewById(R.id.tvSubTotal)

        val db= FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()



        val btnIncrement:Button=view.findViewById(R.id.btnIncrement)
        val btnDecrement:Button=view.findViewById(R.id.btnDecrement)
        val btnAddCart:Button=view.findViewById(R.id.btnAddItemCart)
        var platoEncontrado:Boolean=false
        currentUID = auth.currentUser!!.uid

        //CODIGO PARA OBTENER LOS DATOS DE UN ITEM
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
                            for ((platoNombre, platoData) in (platosMap as? Map<String, Any>
                                ?: emptyMap())) {
                                if(platoNombre==nameItem){
                                    val platoModel = ItemMenu(
                                        imageUrl = (platoData as? Map<String, Any>
                                            ?: emptyMap())["img_url"]?.toString() ?: "",
                                        title = platoNombre,
                                        price = (platoData as? Map<String, Any>
                                            ?: emptyMap())["precio"]?.toString() ?: "",
                                        info = (platoData as? Map<String, Any>
                                            ?: emptyMap())["descripcion"]?.toString() ?: "")
                                    //LLENO VARIABLES GLOBALES
                                    orderImageUrl=platoModel.imageUrl
                                    orderPrice=platoModel.price

                                    //LENO MIS COMPONENTES
                                    llenaDatos(platoModel,ivItem,tvName,description,price,tvCantidad,tvSubTotal)
                                    platoEncontrado=true
                                    break;
                                }

                                if(platoEncontrado==true){
                                    break;
                                }
                            }
                        }
                        Log.i("msg_rest", "Datos del documento en tiempo real: $datos")
                    } else {
                        Log.i("msg_rest", "El documento no existe o está vacío.")
                    }

                }

            }
        }

        //EVENTOS PARA INCREMENTO Y DECREMENTO
        btnIncrement.setOnClickListener {
        increment(price,tvSubTotal,tvCantidad)

        }
        btnDecrement.setOnClickListener{
            decrement(price,tvSubTotal,tvCantidad)
        }

        btnAddCart.setOnClickListener {
            //BLOQUE DE CODIGO PARA CREAR UNA ORDEN
            currentUID = auth.currentUser!!.uid
            // Realizar una consulta para verificar si ya existe un documento con el identificador de usuario y estado activo
            db.collection("orders")
                .whereEqualTo("uid", currentUID)
                .whereEqualTo("estado", "Activo")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        // Ya existe un documento con el identificador de usuario y estado activo
                        val primerDocumento = querySnapshot.documents[0]
                        idOrder = primerDocumento.id.toString()
                        Log.i("OrdenExistente", "Ya existe una orden activa para este usuario.")
                        //BLOQUE DE CODIGO PARA AÑADIR UNA ITEM
                        val orderNameItem = nameItem
                        val orderProveedor=brand_name
                        orderQuantity=tvCantidad.text.toString().toInt()
                        val orderedProduct = ProductOrderModel(currentUID,orderProveedor,orderNameItem,orderImageUrl,orderQuantity,orderPrice)
                        if(orderQuantity>0){
                            addDataToOrdersDatabase(orderedProduct)
                            findNavController().navigate(R.id.action_detalleItemFragment_to_cartFragment)

                        }else{
                            mostrarMsj(requireContext(), "La Cantidad debe ser mayor a 0")
                        }

                    } else {
                        // No existe un documento con el identificador de usuario y estado activo
                        // Puedes proceder a crear una nueva orden
                        val orden = hashMapOf(
                            "brand_name" to brand_name,
                            "uid" to  currentUID,
                            "estado" to "Activo"
                        )
                        db.collection("orders").add(orden)
                            .addOnSuccessListener { documentReference ->
                                idOrder = documentReference.id
                                Log.i("idCreado", "id: $idOrder ")

                                //BLOQUE DE CODIGO PARA AÑADIR UNA ITEM
                                val orderNameItem = nameItem
                                val orderProveedor=brand_name
                                orderQuantity=tvCantidad.text.toString().toInt()
                                val orderedProduct = ProductOrderModel(currentUID,orderProveedor,orderNameItem,orderImageUrl,orderQuantity,orderPrice)
                                if(orderQuantity>0){
                                    addDataToOrdersDatabase(orderedProduct)
                                }else{
                                    mostrarMsj(requireContext(), "La Cantidad debe ser mayor a 0")
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.i("ErrorRestFragment", "Error al crear el documento: $e")
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.i("ErrorRestFragment", "Error al realizar la consulta: $e")
                }


        }
        return view
    }

    private fun llenaDatos(datos_item: ItemMenu, ivImagen: ImageView,
                           tvNombre:TextView, tvDescription:TextView, tvPrecio:TextView,tvCantidad: TextView,tvSubTotal: TextView){
        Picasso.get().load(datos_item.imageUrl).into(ivImagen)
        tvNombre.text=datos_item.title
        tvDescription.text=datos_item.info
        tvCantidad.text="1"
        val priceFormat:String=getMoneda(datos_item.price)
        tvPrecio.text=priceFormat
        tvSubTotal.text=priceFormat
    }

    private fun increment(tvPrice:TextView,tvSubTotal:TextView,tvCantidad:TextView){
        cantidad++
        tvCantidad.text=cantidad.toString()
        //PRECIO
        val strPrecio:String=tvPrice.text.toString()
        val indiceEspacioPrecio = strPrecio.indexOf('.')
        val subcadPrecio = strPrecio.substring(indiceEspacioPrecio + 1)
        val precio=subcadPrecio.toDouble()
        val subTotal:Double=cantidad*precio
        val subTotalFormat=getMoneda(subTotal.toString())
        tvSubTotal.text=subTotalFormat

    }

    private fun decrement(tvPrice:TextView,tvSubTotal:TextView,tvCantidad:TextView){
        if(cantidad>0){
            cantidad--
            tvCantidad.text=cantidad.toString()
            //PRECIO
            val strPrecio:String=tvPrice.text.toString()
            val indiceEspacioPrecio = strPrecio.indexOf('.')
            val subcadPrecio = strPrecio.substring(indiceEspacioPrecio + 1)
            val precio=subcadPrecio.toDouble()
            val subTotal:Double=cantidad*precio
            val subTotalFormat =getMoneda(subTotal.toString())
            tvSubTotal.text=subTotalFormat
        }


    }

    private fun addDataToOrdersDatabase(orderedProduct: ProductOrderModel) {
        val idItem = generateItemId()
        var item: MutableMap<String, Any> = mutableMapOf(
            "imageUrl" to (orderedProduct.imageUrl ?: ""),
            "orderName" to (orderedProduct.orderName ?: ""),
            "price" to (orderedProduct.price?.toString() ?: ""),
            "quantity" to (orderedProduct.quantity?.toString() ?: "")
        )
        orderDatabaseReference.document(idOrder).update(idItem,item)
            .addOnSuccessListener {
                mostrarMsj(requireContext(), "Orden agregado al carrito")
            }
            .addOnFailureListener { e ->
                Log.e("tag_detalle_fragment", "No se encontro el uid:: $idOrder")
                Log.e("tag_detalle_fragment", "Error al actualizar el documento: $e")
            }
    }

    //UTILIDADES
    private fun getMoneda(valor:String):String{
        //convertir a double
        val valorDouble=valor.toDouble()
        // Especifica la moneda que deseas usar
        val moneda=Currency.getInstance("PEN")
        // Crea un formato para la moneda y el idioma específicos
        val formatoMoneda = java.text.NumberFormat.getCurrencyInstance(Locale("es", "PE"))
        formatoMoneda.currency = moneda
        // Formatea el Double como una cantidad de dinero con el símbolo de la moneda
        val cantidadFormateada = formatoMoneda.format(valorDouble)
        return cantidadFormateada
    }
    private fun mostrarMsj(context: Context, mensaje: String) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun generateItemId(): String {

        // Puedes implementar la lógica que desees para generar un ID único.
        // Aquí estoy usando un simple timestamp para propósitos de demostración.
        return UUID.randomUUID().toString()
    }

}