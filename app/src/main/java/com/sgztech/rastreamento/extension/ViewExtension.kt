package com.sgztech.rastreamento.extension

import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

fun View.visible() {
    this.visibility = View.VISIBLE
}


fun View.gone() {
    this.visibility = View.GONE
}

fun SwipeRefreshLayout.gone(){
    this.isRefreshing = false
}

fun SwipeRefreshLayout.visible(){
    this.isRefreshing = true
}