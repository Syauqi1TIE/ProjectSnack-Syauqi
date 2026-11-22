package com.example.sre_snickers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sre_snickers.data.model.OrderModel
import com.example.sre_snickers.databinding.ItemOrderBinding
import com.example.sre_snickers.util.formatTanggal
import com.example.sre_snickers.util.toRupiah

class OrderAdapter(private val list: List<OrderModel>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        OrderViewHolder(ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val o = list[position]
        with(holder.binding) {
            tvOrderId.text = "Pesanan #${o.id}"
            tvOrderStatus.text = o.status
            tvOrderDate.text = formatTanggal(o.created_at)
            tvOrderItems.text = o.order_items.joinToString("\n") { "${it.qty}× ${it.nama}" }
            tvOrderTotal.text = o.total.toRupiah()
            tvOrderPoin.text = "+${o.poin_didapat} poin"
        }
    }

    override fun getItemCount() = list.size
}