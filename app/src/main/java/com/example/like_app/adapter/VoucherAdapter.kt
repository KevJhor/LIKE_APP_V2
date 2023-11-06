package com.example.like_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.model.VoucherModel
import com.squareup.picasso.Picasso

class VoucherAdapter(private var lstChkVoucher: List<VoucherModel>): RecyclerView.Adapter<VoucherAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val ivVoucher: ImageView = itemView.findViewById(R.id.ivVoucher)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_chkdepo, parent, false))
    }

    override fun getItemCount(): Int {
        return lstChkVoucher.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemVoucher = lstChkVoucher[position]
        Picasso.get().load(itemVoucher.ImageUrl).into(holder.ivVoucher)
    }
}