package com.example.restaurantreviewapp.model

import android.view.View

interface OnSpClickListener {
    fun onItemClick(position: Int)
    fun onLongClickListener(position: Int, view: View) {
    }
}