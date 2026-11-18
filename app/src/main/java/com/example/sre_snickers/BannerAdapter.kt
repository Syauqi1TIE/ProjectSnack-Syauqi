package com.example.sre_snickers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sre_snickers.databinding.ItemBannerBinding

data class BannerModel(
    val imageRes: Int,
    val tag: String,
    val title: String,
    val subtitle: String
)

class BannerAdapter(
    private val bannerList: List<BannerModel>
) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemBannerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val item = bannerList[position]
        with(holder.binding) {
            imgBanner.setImageResource(item.imageRes)
            tvBannerTag.text = item.tag
            tvBannerTitle.text = item.title
            tvBannerSubtitle.text = item.subtitle
        }
    }

    override fun getItemCount(): Int = bannerList.size
}