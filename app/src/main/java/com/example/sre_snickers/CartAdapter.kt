package com.example.sre_snickers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sre_snickers.databinding.ItemCartBinding
import com.example.sre_snickers.util.CartItem
import com.example.sre_snickers.util.CartManager
import com.example.sre_snickers.util.loadMenuImage
import com.example.sre_snickers.util.toRupiah

class CartAdapter(private val onChanged: () -> Unit) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    var items: List<CartItem> = emptyList()

    inner class CartViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CartViewHolder(ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]
        val m = item.menu
        with(holder.binding) {
            imgCart.loadMenuImage(m.gambar)
            tvCartName.text = m.nama
            tvCartPrice.text = m.harga.toRupiah()
            tvQty.text = item.qty.toString()
            tvCartSubtotal.text = (m.harga * item.qty).toRupiah()
            btnPlus.setOnClickListener { CartManager.increase(m.id); onChanged() }
            btnMinus.setOnClickListener { CartManager.decrease(m.id); onChanged() }
        }
    }

    override fun getItemCount() = items.size
}