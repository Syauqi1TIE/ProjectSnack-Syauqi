package com.example.sre_snickers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sre_snickers.data.model.PromoModel
import com.example.sre_snickers.databinding.ItemBannerBinding
import com.example.sre_snickers.util.loadMenuImage

class BannerAdapter(private val list: List<PromoModel>) :
    RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(val binding: ItemBannerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BannerViewHolder(ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val item = list[position]
        with(holder.binding) {
            imgBanner.loadMenuImage(item.gambar)
            tvBannerTag.text = item.tag
            tvBannerTitle.text = item.judul
            tvBannerSubtitle.text = item.subjudul ?: ""
        }
    }

    override fun getItemCount() = list.size
}