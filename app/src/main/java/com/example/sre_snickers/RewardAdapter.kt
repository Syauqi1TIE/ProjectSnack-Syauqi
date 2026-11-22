package com.example.sre_snickers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sre_snickers.data.model.RewardModel
import com.example.sre_snickers.databinding.ItemRewardBinding
import com.example.sre_snickers.util.loadMenuImage

class RewardAdapter(
    private val list: List<RewardModel>,
    var userPoin: Int,
    private val onTukar: (RewardModel) -> Unit
) : RecyclerView.Adapter<RewardAdapter.RewardViewHolder>() {

    inner class RewardViewHolder(val binding: ItemRewardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RewardViewHolder(ItemRewardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RewardViewHolder, position: Int) {
        val r = list[position]
        with(holder.binding) {
            imgReward.loadMenuImage(r.gambar)
            tvRewardName.text = r.nama
            tvRewardPoin.text = "${r.poin} poin"
            val cukup = userPoin >= r.poin
            btnTukar.isEnabled = cukup
            btnTukar.text = if (cukup) "Tukar" else "Poin kurang"
            btnTukar.setOnClickListener { onTukar(r) }
        }
    }

    override fun getItemCount() = list.size
}