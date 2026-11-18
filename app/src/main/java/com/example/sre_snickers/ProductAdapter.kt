package com.example.sre_snickers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sre_snickers.databinding.ItemProductBinding

class ProductAdapter(
    private val productList: List<ProductModel>,
    private val onAddClick: (ProductModel) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(
        val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {

        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        val item = productList[position]

        with(holder.binding) {
            tvProductName.text = item.name

            tvProductPrice.text =
                "Rp ${"%,d".format(item.price).replace(',', '.')}"

            imgProduct.setImageResource(item.imageRes)

            btnAddProduct.setOnClickListener {
                onAddClick(item)
            }
        }
    }

    override fun getItemCount(): Int = productList.size
}
