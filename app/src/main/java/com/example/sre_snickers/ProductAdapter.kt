package com.example.sre_snickers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sre_snickers.data.model.MenuModel
import com.example.sre_snickers.databinding.ItemProductBinding
import com.example.sre_snickers.util.loadMenuImage
import com.example.sre_snickers.util.toRupiah

class ProductAdapter(
    private val list: List<MenuModel>,
    private val onAddClick: (MenuModel) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProductViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = list[position]
        with(holder.binding) {
            imgProduct.loadMenuImage(item.gambar)
            tvProductCategory.text = item.kategori
            tvProductName.text = item.nama
            tvProductPrice.text = item.harga.toRupiah()
            btnAddProduct.setOnClickListener { onAddClick(item) }
        }
    }

    override fun getItemCount() = list.size
}