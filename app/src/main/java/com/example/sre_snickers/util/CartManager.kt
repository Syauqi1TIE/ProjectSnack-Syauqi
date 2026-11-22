package com.example.sre_snickers.util

import com.example.sre_snickers.data.model.MenuModel

data class CartItem(val menu: MenuModel, var qty: Int)

object CartManager {
    private val items = linkedMapOf<Long, CartItem>()

    fun add(menu: MenuModel) { items.getOrPut(menu.id) { CartItem(menu, 0) }.qty++ }
    fun increase(id: Long) { items[id]?.let { it.qty++ } }
    fun decrease(id: Long) {
        val item = items[id] ?: return
        item.qty--
        if (item.qty <= 0) items.remove(id)
    }
    fun clear() = items.clear()
    fun list(): List<CartItem> = items.values.toList()
    fun totalQty(): Int = items.values.sumOf { it.qty }
    fun totalPrice(): Int = items.values.sumOf { it.menu.harga * it.qty }
}