package com.example.sre_snickers

import android.view.View
import com.example.sre_snickers.databinding.ActivityListBinding

fun ActivityListBinding.showLoading() {
    progressList.visibility = View.VISIBLE
    layoutState.visibility = View.GONE
    rvList.visibility = View.GONE
}

fun ActivityListBinding.showList() {
    progressList.visibility = View.GONE
    layoutState.visibility = View.GONE
    rvList.visibility = View.VISIBLE
}

fun ActivityListBinding.showMessage(text: String, withRetry: Boolean) {
    progressList.visibility = View.GONE
    rvList.visibility = View.GONE
    layoutState.visibility = View.VISIBLE
    tvState.text = text
    btnRetry.visibility = if (withRetry) View.VISIBLE else View.GONE
}