package com.example.like_app.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.model.CartModel
import com.squareup.picasso.Picasso
import java.util.Currency
import java.util.Locale
//private val listener:RecyclerViewEvent
class CartAdapter(  private var list:List<CartModel>) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    var cartAdapterListener: CartAdapterListener? = null
    var count:Int = 0
    var amount:Double=0.00
    //View.OnClickListener
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)  {

        val ivItemCart: ImageView = itemView.findViewById(R.id.ivItemCart)
        val tvTitle:TextView=itemView.findViewById(R.id.tvTitleItemCart)
        val tvItemPriceCart:TextView=itemView.findViewById(R.id.tvPriceItemCart)
        val tvSubTotalCart:TextView=itemView.findViewById(R.id.tvSubTotalItemCart)
        val btnIncrement:Button=itemView.findViewById(R.id.btnCartItemAdd)
        val btnDecrement:Button=itemView.findViewById(R.id.btnCartItemMinus)
        val tvCartItemCount:TextView=itemView.findViewById(R.id.tvCartItemCount)

    }

    interface RecyclerViewEvent {
        fun onItemClick(position:Int)
    }
    interface CartAdapterListener {
        fun onCartTotalChanged(total: Double)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflateView:View=LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return ViewHolder(inflateView)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemCart= list[position]
        Picasso.get().load(itemCart.imageUrl).into(holder.ivItemCart)
        holder.tvTitle.text=itemCart.orderName
        holder.tvItemPriceCart.text=itemCart.price
        holder.tvCartItemCount.text=itemCart.quantity.toString()
        count=holder.tvCartItemCount.text.toString().toInt()
        setSubTotal(holder.tvSubTotalCart,count,itemCart.price.toDouble())
        Log.i("tagPoistion","$position")
        holder.btnIncrement.setOnClickListener {
            count++
            amount= amount+getSubTotal(1,itemCart.price.toDouble())
            holder.tvCartItemCount.text=count.toString()
            setSubTotal(holder.tvSubTotalCart,count,itemCart.price.toDouble())
            notifyTotalChanged()
        }

        holder.btnDecrement.setOnClickListener {
            if(count>0){
                count--
                amount= amount-getSubTotal(1,itemCart.price.toDouble())
                holder.tvCartItemCount.text=count.toString()
                setSubTotal(holder.tvSubTotalCart,count,itemCart.price.toDouble())
                notifyTotalChanged()
            }


        }
    }

    fun getSubTotal(cantidad:Int,precio:Double):Double {
        val subTotal = cantidad * precio
        return  subTotal
    }

    fun setSubTotal(tvSubTotal:TextView,cantidad:Int,precio:Double){
        val subTotal=getSubTotal(cantidad,precio)
        val moneda=Currency.getInstance("PEN")
        val formatoMoneda = java.text.NumberFormat.getCurrencyInstance(Locale("es", "PE"))
        formatoMoneda.currency = moneda
        val cantidadFormateada = formatoMoneda.format(subTotal)
        tvSubTotal.text=cantidadFormateada
    }

    fun notifyTotalChanged() {
        cartAdapterListener?.onCartTotalChanged(calcularCantidadTotal())
    }
    fun calcularCantidadTotal(): Double {
        var cantidadTotal = amount

        return cantidadTotal
    }
    fun getMonto():Double{
        var monto:Double=0.00
        for (item in list){
            val subTotal=item.price.toDouble()*item.quantity
            monto+=subTotal
        }
        amount=monto
        return monto
    }



}