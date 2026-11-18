package com.example.sre_snickers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MenuTabsAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val categories = listOf("Semua", "Coffee", "Pastry", "Cake")

    override fun getItemCount(): Int = categories.size

    override fun createFragment(position: Int): Fragment {
        return MenuCategoryFragment.newInstance(categories[position])
    }
}