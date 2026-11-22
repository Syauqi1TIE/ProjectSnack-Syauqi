package com.example.sre_snickers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sre_snickers.data.model.PromoModel
import com.example.sre_snickers.databinding.ItemPromoBinding
import com.example.sre_snickers.util.loadMenuImage

class PromoAdapter(private val list: List<PromoModel>) :
    RecyclerView.Adapter<PromoAdapter.PromoViewHolder>() {

    inner class PromoViewHolder(val binding: ItemPromoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PromoViewHolder(ItemPromoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: PromoViewHolder, position: Int) {
        val p = list[position]
        with(holder.binding) {
            imgBanner.loadMenuImage(p.gambar)
            tvBannerTag.text = p.tag
            tvBannerTitle.text = p.judul
            tvBannerSubtitle.text = p.subjudul ?: ""
        }
    }

    override fun getItemCount() = list.size
}